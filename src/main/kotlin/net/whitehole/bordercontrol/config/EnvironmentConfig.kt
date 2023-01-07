package net.whitehole.bordercontrol.config

import dev.schlaubi.envconf.Config

class Config : Config("BC_") {
    val DEBUG by getEnv(true) { it.toBoolean() }
}