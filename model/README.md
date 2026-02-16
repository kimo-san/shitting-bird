# Materials
## Electronics
* 1 Arduino Nano microcontroller
* 1 HM-10 bluetooth module
* 4 TIP120 NPN Darlington transistors
* Jumper wires
* Heat shrink tubing
* Soldering iron
## Motors
* 2 Rotating DC motors
* 2 DC motor–based pumps
## Power Supply
* 1 Battery holder (minimum 5V output)
## Fabrication
* 3D printer
* Tools for model post-processing (e.g., cutting, sanding, fitting)

# Arduino Pinout
|   Pin   | Description |
|---------|-------------|
| **GND** | common ground (shared with all transistors and battery negative terminal) |
| **D2**  | controls transistor connected to the slurry pump |
| **D3**  | controls transistor connected to the powder dispenser |
| **D4**  | controls transistor connected to the mixer motor |
| **D5**  | controls transistor connected to the water pump |
| **D10** | TX (receives data from Bluetooth module RX) |
| **D11** | RX (sends data to Bluetooth module TX) |
| **3V3** | power supply for Bluetooth module |
| **GND** | ground for Bluetooth module (connected to common ground) |

# Schemas
## Transistor block
![](https://raw.githubusercontent.com/kimo-san/shitting-bird/refs/heads/main/model/schema1.png)
## Water pump
![](https://raw.githubusercontent.com/kimo-san/shitting-bird/refs/heads/main/model/schema5.png)
## Powder dispenser
![](https://raw.githubusercontent.com/kimo-san/shitting-bird/refs/heads/main/model/schema2.png)
## Mixer
![](https://raw.githubusercontent.com/kimo-san/shitting-bird/refs/heads/main/model/schema3.png)
## Slurry pump
![](https://raw.githubusercontent.com/kimo-san/shitting-bird/refs/heads/main/model/schema4.png)
