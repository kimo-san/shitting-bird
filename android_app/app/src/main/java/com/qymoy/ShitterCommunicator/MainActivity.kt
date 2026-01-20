package com.qymoy.ShitterCommunicator

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.window.Dialog
import com.qymoy.ShitterCommunicator.data.BluetoothControllerImpl
import com.qymoy.ShitterCommunicator.presentation.MainContent
import com.qymoy.ShitterCommunicator.presentation.MainViewModel
import com.qymoy.ShitterCommunicator.ui.theme.ShitterCommunicatorTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val setup = SetupBluetooth(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ShitterCommunicatorTheme {

                val status = remember { setup.run() }

                if (status != SetupBluetooth.Messages.SUCCESS) {
                    HandleBluetoothStatusMessage(status)
                } else {
                    val vm by viewModels<MainViewModel> {
                        MainViewModel.factory(BluetoothControllerImpl(applicationContext))
                    }
                    MainContent(vm)
                }
            }
        }
    }

    /**
     * Set up permissions and enable bluetooth
     */
    private class SetupBluetooth(val context: ComponentActivity) {

        enum class Messages { SUCCESS, BLUETOOTH_UNSUPPORTED, PERMISSION_DENIED, BLUETOOTH_DISABLED }
        fun run() = with(context) {

            val manager = applicationContext.getSystemService(BluetoothManager::class.java)
            val adapter = manager?.adapter
                ?: return@with Messages.BLUETOOTH_UNSUPPORTED

            askPermissions()
            if (!hasPermissions)
                return Messages.PERMISSION_DENIED

            askEnableBluetooth()
            if (!adapter.isEnabled)
                return Messages.BLUETOOTH_DISABLED

            return Messages.SUCCESS
        }

        fun launchPermissionSettings() = with(context) {
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            startActivity(intent)
        }

        fun askEnableBluetooth() {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            enableBluetoothLauncher.launch(intent)
        }

        fun askPermissions() {
            permissionLauncher.launch(permissionList)
        }

        private val permissionList =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            } else {
                arrayOf(
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN
                )
            }

        private val enableBluetoothLauncher = context.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { /* not needed */ }
        private val permissionLauncher = context.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { }
        private val hasPermissions get() = permissionList.all {
            context.checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }

    @Composable
    private fun HandleBluetoothStatusMessage(status: SetupBluetooth.Messages) {
        Surface {

            Dialog({ }) {
                Column {

                    val text = when (status) {

                        SetupBluetooth.Messages.PERMISSION_DENIED ->
                            "Please, enable permission to see devices around " +
                                    "in app settings to proceed"

                        SetupBluetooth.Messages.BLUETOOTH_UNSUPPORTED ->
                            "Your android device does not support bluetooth."

                        SetupBluetooth.Messages.BLUETOOTH_DISABLED ->
                            "Please, enable bluetooth to proceed"

                        SetupBluetooth.Messages.SUCCESS -> "Unknown error"
                    }


                    val (action, buttonText) = when (status) {
                        SetupBluetooth.Messages.PERMISSION_DENIED -> {
                            { setup.launchPermissionSettings() } to "Open settings"
                        }
                        SetupBluetooth.Messages.BLUETOOTH_DISABLED -> {
                            { setup.askEnableBluetooth() } to "Enable bluetooth"
                        }
                        else -> {
                            { restartApp() } to "Restart app"
                        }
                    }


                    Text(text)
                    HorizontalDivider()
                    Button(action) { Text(buttonText) }
                    val (exit, exitTitle) = ::restartApp to "Restart app"
                    Button(exit) { Text(exitTitle) }
                }
            }
        }
    }
}

fun Context.restartApp() {
    val intent = applicationContext.packageManager
        .getLaunchIntentForPackage(applicationContext.packageName)
        ?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    applicationContext.startActivity(intent)
    exitProcess(0)
}