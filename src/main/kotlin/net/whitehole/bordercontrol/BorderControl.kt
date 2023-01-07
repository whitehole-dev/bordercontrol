package net.whitehole.bordercontrol

import mu.KotlinLogging
import net.whitehole.bordercontrol.config.Config
import net.whitehole.bordercontrol.server.RestApi
import org.slf4j.simple.SimpleLogger


class BorderControl {
    private val restApi = RestApi(this)
    private val logger = KotlinLogging.logger {}
    val config = Config()
    init {
        System.setProperty("logging.level.net.whitehole.bordercontrol", "DEBUG")
        logger.info { "Hello World" }
        logger.debug { "Hello Debugger" }
        restApi.launch()
    }
}

fun main() {
    println("Starting Bordercontrol")
    BorderControl()
}