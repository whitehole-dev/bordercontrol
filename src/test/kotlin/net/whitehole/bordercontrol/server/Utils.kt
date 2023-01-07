package net.whitehole.bordercontrol.server

import io.ktor.server.testing.*

fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) {
    testApplication {
        application {
            RestApi.module.invoke(this)
        }
        block.invoke(this)
    }
}