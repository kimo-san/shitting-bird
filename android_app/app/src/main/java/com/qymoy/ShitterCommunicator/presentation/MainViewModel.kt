package com.qymoy.ShitterCommunicator.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.qymoy.ShitterCommunicator.domain.BluetoothController
import com.qymoy.ShitterCommunicator.domain.BluetoothDevice
import com.qymoy.ShitterCommunicator.domain.Commands
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MainViewModel (
    private val bluetoothController: BluetoothController
): ViewModel() {

    private val scope = CoroutineScope(
        viewModelScope.coroutineContext + SupervisorJob()
    )

    private val _bluetoothState = MutableStateFlow(BluetoothUiState())

    val state = combine(
        bluetoothController.scannedDevices,
        _bluetoothState,
        bluetoothController.connectedTo
    ) { sc, st, dev ->
        st.copy(
            scannedDevs = sc,
            connected = dev
        )
    }.stateIn(scope, SharingStarted.WhileSubscribed(5000), _bluetoothState.value)

    val messages = MutableStateFlow(emptyList<String>())

    fun send(mess: Commands) = scope.launch {
        try {
            bluetoothController.sendCommand(mess)
        } catch (_: RuntimeException) {
            println("Unlucky try to send message to blu-device")
            bluetoothController.connectedTo.value?.let {
                bluetoothController.disconnect(it)
            }
        }
    }

    fun startDisc() = bluetoothController.startDiscovery()
    fun stopDisc() = bluetoothController.stopDiscovery()
    fun connectTo(device: BluetoothDevice) = scope.launch {
        bluetoothController.connect(device)
        stopDisc()
    }
    fun disconnect(device: BluetoothDevice) = scope.launch {
        bluetoothController.disconnect(device)
        startDisc()
    }

    init {
        bluetoothController.receivedMessages
            .onEach { messages.update { l -> l + it } }
            .launchIn(scope)
    }

    companion object {
        fun factory(
            bluetoothController: BluetoothController
        ) = viewModelFactory {
            initializer { MainViewModel(bluetoothController) }
        }
    }
}

