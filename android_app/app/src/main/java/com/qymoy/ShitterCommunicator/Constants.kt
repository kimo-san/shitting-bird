package com.qymoy.ShitterCommunicator

import com.qymoy.ShitterCommunicator.domain.Command
import java.util.UUID

/**
 * Вручную
 */
val commands = listOf(
    Command(
        name = "Execute main program",
        command = "d"
    ),
    Command(
        name = "Run cleaning",
        command = "cl"
    ),
    Command(
        name = "Water-in test",
        command = "w"
    ),
    Command(
        name = "Cream-out test",
        command = "r"
    ),
    Command(
        name = "Mix test",
        command = "m"
    ),
    Command(
        name = "Pin test",
        command = "p"
    ),
    Command(
        name = "Cancel execution",
        command = "c"
    )
)

/**
 * # UUID сервиса данных
 * Сервис, который отвечает за общий бус данных с модуля на устройсво и обратно.
 */
val serviceUuid = UUID.fromString("0000FFE0-0000-1000-8000-00805F9B34FB")

/**
 * # UUID характеристики данных
 * Конкретная характеристика потока данных
 */
val charUuid = UUID.fromString("0000FFE1-0000-1000-8000-00805F9B34FB")

/**
 * # UUID дескриптора оповещений
 * *Client Characteristic Configuration Descriptor*
 *
 * Дексриптор для включения оповещений от модуля, дабы он присылал данные на устройство.
 * Этот декскриптор по стандарту всегда имеет такой UUID.
 */
val cccdUuid = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")