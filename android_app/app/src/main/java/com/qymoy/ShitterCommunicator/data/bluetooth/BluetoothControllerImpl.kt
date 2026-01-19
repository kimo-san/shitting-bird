package com.qymoy.ShitterCommunicator.data.bluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import com.qymoy.ShitterCommunicator.charUuid
import com.qymoy.ShitterCommunicator.data.getCommand
import com.qymoy.ShitterCommunicator.domain.BluetoothController
import com.qymoy.ShitterCommunicator.domain.BluetoothDeviceDomain
import com.qymoy.ShitterCommunicator.domain.Commands
import com.qymoy.ShitterCommunicator.hasPermission
import com.qymoy.ShitterCommunicator.serviceUuid
import com.qymoy.ShitterCommunicator.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
class BluetoothControllerImpl(
    private val context: Context
): BluetoothController {

    private val deviceGatt = MutableStateFlow<BluetoothGatt?>(null)

    override val scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val receivedMessages = MutableStateFlow("")
    override val connectedTo = MutableStateFlow<BluetoothDeviceDomain?>(null)


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun connect(device: com.qymoy.ShitterCommunicator.domain.BluetoothDevice) {
        bluetoothAdapter?.getRemoteDevice(device.address)?.bluetoothGatt()
    }

    override suspend fun disconnect(device: com.qymoy.ShitterCommunicator.domain.BluetoothDevice) {
        connectedTo.update { null }
        deviceGatt.value?.close()
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override suspend fun sendCommand(command: Commands): Boolean =
        suspendCancellableCoroutine { continuation ->
            val gatt = deviceGatt.value ?: error("caught! there is no gatt!")

            val char = gatt
                .getService(serviceUuid)
                .getCharacteristic(charUuid)

            println(char.writeType)

            gatt.writeCharacteristic(
                char,
                command.getCommand().toByteArray(Charsets.US_ASCII),
                char.writeType
            )

            val received = gatt.readCharacteristic(char)

            continuation.resume(received)
            continuation.invokeOnCancellation {
                println("Delivered: canceled!")
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
}