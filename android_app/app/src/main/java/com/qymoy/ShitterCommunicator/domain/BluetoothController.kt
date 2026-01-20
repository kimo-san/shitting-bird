package com.qymoy.ShitterCommunicator.domain

import kotlinx.coroutines.flow.StateFlow

interface BluetoothController {

    val scannedDevices: StateFlow<List<BluetoothDevice>>
    fun startDiscovery()
    fun stopDiscovery()

    val connectedTo: StateFlow<BluetoothDevice?>
    suspend fun connect(device: BluetoothDevice)
    suspend fun disconnect(device: BluetoothDevice)

    val receivedMessages: StateFlow<String>
    suspend fun sendCommand(command: Command): Boolean

}