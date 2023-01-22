package net.whitehole.bordercontrol.config

import dev.schlaubi.envconf.Config

class Config : Config("BC_") {
    val DEBUG by getEnv(true) { it.toBoolean() }
    val MONGO_URI by getEnv("DEFAULT")
    init {
        if (MONGO_URI.isBlank() && MONGO_URI != "DEFAULT")
            throw NullPointerException("BC_MONGO_URI not set! Can\'t work without Database")
    }
}