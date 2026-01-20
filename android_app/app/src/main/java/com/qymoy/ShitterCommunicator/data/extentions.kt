package com.qymoy.ShitterCommunicator.data

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import com.qymoy.ShitterCommunicator.domain.BluetoothDeviceDomain

fun Context.hasPermission(permission: String): Boolean =
    checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

@Suppress("MissingPermission")
fun BluetoothDevice.toDomain(): BluetoothDeviceDomain =
    BluetoothDeviceDomain(
        name = try { name } catch (_: Throwable) {
            "Permission BLUETOOTH_CONNECT is required to show the devices' names"
        },
        address = address
    )