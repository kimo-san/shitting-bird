package com.qymoy.ShitterCommunicator.presentation

import com.qymoy.ShitterCommunicator.domain.BluetoothDevice

data class BluetoothUiState(
    val connected: BluetoothDevice? = null,
    val scannedDevs: List<BluetoothDevice> = emptyList()
)