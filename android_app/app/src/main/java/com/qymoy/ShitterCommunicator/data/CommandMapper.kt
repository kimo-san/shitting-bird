package com.qymoy.ShitterCommunicator.data

import com.qymoy.ShitterCommunicator.domain.Commands
import com.qymoy.ShitterCommunicator.domain.Commands.*

fun Commands.getCommand(): String =
    when (this) {
        EXECUTE_CMD -> "d"
        CLEAN_CMD -> "cl"
        WATER_TEST_CMD -> "w"
        CREAM_TEST_CMD -> "r"
        MIX_TEST_CMD -> "m"
        PIN_TEST_CMD -> "p"
        CANCEL_CMD -> "c"
    }