package com.qymoy.ShitterCommunicator.presentation

import com.qymoy.ShitterCommunicator.domain.Commands

val Commands.displayName get() =
    when (this) {
        Commands.EXECUTE_CMD -> "Execute main program"
        Commands.CLEAN_CMD -> "Run cleaning"
        Commands.WATER_TEST_CMD -> "Water-in test"
        Commands.CREAM_TEST_CMD -> "Cream-out test"
        Commands.MIX_TEST_CMD -> "Mix test"
        Commands.PIN_TEST_CMD -> "Pin test"
        Commands.CANCEL_CMD -> "Cancel execution"
        else -> "Run '$name'" // Просто показываем имя объекта перечисления
    }