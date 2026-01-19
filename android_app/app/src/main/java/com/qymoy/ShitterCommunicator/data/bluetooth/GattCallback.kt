package com.qymoy.ShitterCommunicator.data.bluetooth

import android.Manifest
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresPermission
import com.qymoy.ShitterCommunicator.cccdUuid
import com.qymoy.ShitterCommunicator.charUuid
import com.qymoy.ShitterCommunicator.hasPermission
import com.qymoy.ShitterCommunicator.serviceUuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.launch
import kotlin.collections.find
import kotlin.error

open class GattCallback(

    private val context: Context,

    private val gattFlow: FlowCollector<BluetoothGatt>,
    private val messagesFlow: FlowCollector<String>,

    private val scope: CoroutineScope

): BluetoothGattCallback() {

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) scope.launch {
            gattFlow.emit(gatt)
            gatt.discoverServices()
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            val services = gatt!!.services

            val service = services.find { it.uuid == serviceUuid }
                ?: error("Caught! There is no service!")

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

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun enableNotifications(
        gatt: BluetoothGatt,
        service: BluetoothGattService
    ) {
        val char = service.getCharacteristic(charUuid)
            ?: error("Caught! There are no characteristics!")

        val okSet = gatt.setCharacteristicNotification(char, true)
        if (!okSet)
            error("Caught! There is no characteristic for notifications!")

        val desc = char.getDescriptor(cccdUuid)
            ?: error("Caught! There is no descriptor!")

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