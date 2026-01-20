package com.qymoy.ShitterCommunicator.data

import android.Manifest
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothStatusCodes
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.qymoy.ShitterCommunicator.cccdUuid
import com.qymoy.ShitterCommunicator.charUuid
import com.qymoy.ShitterCommunicator.domain.BluetoothController
import com.qymoy.ShitterCommunicator.domain.BluetoothDeviceDomain
import com.qymoy.ShitterCommunicator.domain.Command
import com.qymoy.ShitterCommunicator.serviceUuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * # Имплементация контроллера блютуза через Гатт.
 * Требует разрешений такие как:
 *  - `android.Manifest.permission.BLUETOOTH_CONNECT`
 *  - `android.Manifest.permission.BLUETOOTH_SCAN`
 *
 * ---
 * ## НЕ РАБОАТЕТ!!!!: Немного о UUID
 *
 * Если к вашему устройству вы вроде бы подключились, но не можете присылать сообщения,
 * то проблема может заключаться в том, что ниже представленные UUID не подходять вашему модулю.
 *
 * Самый простой способ узнать UUID сервиса - скачать приложение nRF Connect скопировать их.
 *
 * ---
 * ### TLDR: Что это вообще?
 *
 * Грубо говоря, ниже указанные ***UUID - это специальные ключи***, чтобы общаться с модулем.
 * В общем, блютуз-устройства имеют свои ***сервисы***, которые представляют собой группу разных
 * ***характеристик***. Например, фитнесс браслеты могут иметь два сервиса: один для передачи
 * данных о пульсе и один - для передачи уровня заряда батареи.
 *
 * Как говорилось ранее, в сервисах хранятся характеристики. ***Характеристики*** - это контейнеры
 * конкретных потоков данных. Например, конкретная характеристика уровня заряда батареи или частоты
 * пульса. Они хранят зачения, а также обладают ***дескрипторами***.
 *
 * ***Дескрипторы*** отвечают за метаданные. Как правило, UUID таких дескрипторов
 * стандартизированны. Зачастую к ним обращаются, если нужно получать сообщения от модуля.
*/
@Suppress("MissingPermission")
class BluetoothControllerImpl(
    private val context: Context
): BluetoothController {

    private val deviceGatt = MutableStateFlow<BluetoothGatt?>(null)
    private val bluetoothManager by lazy { context.getSystemService(BluetoothManager::class.java) }
    private val bluetoothAdapter by lazy { bluetoothManager?.adapter }
    private val foundDeviceReceiver by lazy {
        FoundDeviceReceiver { device ->
            scannedDevices.update { devices ->
                val newDevice: BluetoothDeviceDomain = device.toDomain()
                if (newDevice in devices) devices else devices + newDevice
            }
        }
    }

    override val scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val receivedMessages = MutableStateFlow("")
    override val connectedTo = MutableStateFlow<BluetoothDeviceDomain?>(null)

    override suspend fun connect(device: BluetoothDeviceDomain) {
        bluetoothAdapter?.getRemoteDevice(device.address)?.bluetoothGatt()
    }
    override suspend fun disconnect(device: BluetoothDeviceDomain) {
        connectedTo.update { null }
        deviceGatt.value?.close()
    }
    override suspend fun sendCommand(command: Command): Boolean =
        suspendCancellableCoroutine { continuation ->

            val gatt = deviceGatt.value
                ?: error("Caught! There is no gatt!")
            val service = gatt.getService(serviceUuid)
                ?: error("Caught! There is no passing service!")
            val char = service.getCharacteristic(charUuid)
                ?: error("Caught! There is no passing characteristic!")

            val sentSuccessfully = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gatt.writeCharacteristic(
                    char,
                    command.command.toByteArray(Charsets.US_ASCII),
                    char.writeType
                ) == BluetoothStatusCodes.SUCCESS
            } else {
                gatt.writeCharacteristic(char)
            }

            println("Message delivery: " + if (sentSuccessfully) "success" else "failure")

            continuation.resume(sentSuccessfully)
            continuation.invokeOnCancellation {
                println("Message delivery: canceled!")
            }
        }

    override fun startDiscovery() {
        if (!context.hasPermission(Manifest.permission.BLUETOOTH_SCAN))
            return

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(foundDeviceReceiver, filter)

        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!context.hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
            return
        try {
            context.unregisterReceiver(foundDeviceReceiver)
        } catch (_: IllegalArgumentException) {
            println("Reciever was unregistered before start")
        }
        scannedDevices.update { emptyList() }
        bluetoothAdapter?.cancelDiscovery()
    }

    private suspend fun BluetoothDevice.bluetoothGatt(): Result<BluetoothGatt> {

        return suspendCancellableCoroutine { continuation ->

            val scope = CoroutineScope(continuation.context)

            val callback = object : GattCallback(
                gattFlow = deviceGatt,
                messagesFlow = receivedMessages,
                context = context,
                scope = scope
            ) {
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt,
                    status: Int,
                    newState: Int
                ) {
                    super.onConnectionStateChange(gatt, status, newState)
                    when (status) {
                        BluetoothGatt.GATT_SUCCESS -> {
                            connectedTo.update { gatt.device.toDomain() }
                            continuation.resume(Result.success(gatt))
                        }

                        BluetoothGatt.GATT_FAILURE -> {
                            connectedTo.update { null }
                            continuation.resume(Result.failure(Exception("Connection failed")))
                        }
                    }
                }
            }

            val gatt = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                connectGatt(
                    context, // context
                    false, // autoConnect
                    callback, // callback
                    BluetoothDevice.TRANSPORT_AUTO, // transport
                    BluetoothDevice.PHY_LE_1M // phy
                )
            else connectGatt(context, false, callback)

            continuation.invokeOnCancellation {
                scope.cancel("Gatt was cancelled with an exception", it)
                gatt.close()
            }
        }
    }

    private class FoundDeviceReceiver(
        private val onDeviceFound: (BluetoothDevice) -> Unit
    ): BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            when(intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {

                    val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        intent.getParcelableExtra(
                            BluetoothDevice.EXTRA_DEVICE,
                            BluetoothDevice::class.java
                        )
                    } else {
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    }

                    device?.let(onDeviceFound)
                }
            }
        }
    }

    private open class GattCallback(
        private val context: Context,
        private val gattFlow: FlowCollector<BluetoothGatt>,
        private val messagesFlow: FlowCollector<String>,
        private val scope: CoroutineScope
    ): BluetoothGattCallback() {

        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) scope.launch {
                gattFlow.emit(gatt)
                gatt.discoverServices()
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                val services = gatt!!.services

                val service = services.find { it.uuid == serviceUuid } ?:
                    error("Caught! There is no service!")

                if (context.hasPermission(Manifest.permission.BLUETOOTH_CONNECT))
                    enableNotifications(gatt, service)
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            scope.launch {
                messagesFlow.emit(value.toString(Charsets.US_ASCII))
            }
        }

        private fun enableNotifications(
            gatt: BluetoothGatt,
            service: BluetoothGattService
        ) {
            val char = service.getCharacteristic(charUuid) ?:
                error("Caught! There are no characteristics!")

            val notificationsEnabled = gatt.setCharacteristicNotification(char, true)
            if (!notificationsEnabled)
                error("Caught! There is no characteristic for notifications!")

            val desc = char.getDescriptor(cccdUuid) ?:
                error("Caught! There is no descriptor!")

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gatt.writeDescriptor(
                    desc,
                    BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                )
            }
            else {
                desc.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                gatt.writeDescriptor(desc)
            }
        }
    }
}