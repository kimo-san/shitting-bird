package com.qymoy.ShitterCommunicator

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.qymoy.ShitterCommunicator.data.bluetooth.BluetoothControllerImpl
import com.qymoy.ShitterCommunicator.presentation.MainContent
import com.qymoy.ShitterCommunicator.presentation.MainViewModel
import com.qymoy.ShitterCommunicator.ui.theme.ShitterCommunicatorTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val bluetoothManager by lazy {
            applicationContext.getSystemService(BluetoothManager::class.java)
        }
        val bluetoothAdapter by lazy { bluetoothManager?.adapter }

        val isBluetoothEnabled: Boolean = bluetoothAdapter?.isEnabled == true

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permitted ->

            val canEnableBluetooth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permitted[Manifest.permission.BLUETOOTH_CONNECT] == true
            } else false

            if (canEnableBluetooth && !isBluetoothEnabled) {

                val enableBluetoothLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { /* not needed */ }

                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        }

        enableEdgeToEdge()
        setContent {
            ShitterCommunicatorTheme {
                val vm by viewModels<MainViewModel> {
                    MainViewModel.factory(BluetoothControllerImpl(applicationContext))
                }
                MainContent(vm)
            }
        }
    }
}