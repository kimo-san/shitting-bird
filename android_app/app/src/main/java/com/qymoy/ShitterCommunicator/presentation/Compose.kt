package com.qymoy.ShitterCommunicator.presentation

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.qymoy.ShitterCommunicator.R
import com.qymoy.ShitterCommunicator.domain.BluetoothDevice
import com.qymoy.ShitterCommunicator.domain.Commands

private fun toast(context: Context, text: String) =
    Toast.makeText(context, text, Toast.LENGTH_SHORT)

@Composable
fun MainContent(vm: MainViewModel) {

    val state by vm.state.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(state.connected) {
        val message = when (state.connected) {
            null -> "Disconnected"
            else -> "Connected"
        }
        toast(context, message).show()
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->

        Column(Modifier.padding(innerPadding)) {

            if (state.connected != null) {
                BackHandler { vm.disconnect(state.connected!!) }
                ConnectedScreen(
                    device = state.connected!!,
                    send = { vm.send(it) },
                    disconnect = vm::disconnect,
                    terminalText = vm.messages.collectAsState().value
                )

            }
            else {

                var scanning by remember { mutableStateOf(false) }
                if (scanning) {
                    TextButton({ vm.stopDisc(); scanning = false }) {
                        Text("Stop scan")
                    }
                } else {
                    TextButton({ vm.startDisc(); scanning = true }) {
                        Text("Enable scan")
                    }
                }

                HorizontalDivider()

                DeviceList(
                    list = state.scannedDevs,
                    connectToDevice = vm::connectTo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                )
            }
        }
    }
}

@Composable
fun ConnectedScreen(
    device: BluetoothDevice,
    send: (Commands) -> Unit,
    disconnect: (BluetoothDevice) -> Unit,
    terminalText: List<String>
) {
    Column(
        Modifier
            .fillMaxSize()
            .safeContentPadding(),
        Arrangement.SpaceAround
    ) {

        Column(
            Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                "Connected to " + (device.name ?: "Unnamed"),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                device.address, Modifier.alpha(0.5f),
                style = MaterialTheme.typography.headlineSmall,
            )
            Image(
                painterResource(R.drawable.birds_for_beginners), null,
                Modifier.align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Inside
            )
        }

        Column(
            Modifier.weight(1f),
            Arrangement.spacedBy(8.dp)
        ) {
            CommandBlock(
                send,
                Modifier.heightIn(max = 200.dp)
            )
            Terminal(
                terminalText,
                Modifier.heightIn(max = 200.dp)
            )
        }

        OutlinedButton(
            { send(Commands.CANCEL_CMD); disconnect(device) },
            Modifier.padding(8.dp)
        ) {
            Text("Disconnect")
        }
    }
}

@Composable
private fun CommandBlock(
    send: (Commands) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier) {

        LazyVerticalGrid(
            GridCells.Fixed(2),
            Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(Commands.entries) {
                Button({ send(it) }) {
                    Text(
                        it.displayName,
                        Modifier.fillMaxWidth(),
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun Terminal(
    text: List<String>,
    modifier: Modifier = Modifier
) {
    Card(modifier) {
        val state = rememberScrollState()
        Text(
            text.joinToString(separator = "") { it },
            Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(state)
                .padding(16.dp),
            overflow = TextOverflow.Visible
        )
    }
}

@Composable
private fun DeviceList(
    list: List<BluetoothDevice>,
    connectToDevice: (BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier) {
        items(list) {
            ListItem(
                headlineContent = { Text(it.name ?: "Unnamed") },
                supportingContent = { Text(it.address) },
                modifier = Modifier.clickable { connectToDevice(it) }
            )
        }
    }
}