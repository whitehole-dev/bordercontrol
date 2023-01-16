package net.whitehole.bordercontrol.server

import io.ktor.server.testing.*
import net.whitehole.bordercontrol.setup.borderControl
import net.whitehole.bordercontrol.setup.setupDatabase

fun testApp(block: suspend ApplicationTestBuilder.() -> Unit) {
    if (borderControl == null)
        setupDatabase()
    testApplication {
        application {
            RestApi.module.invoke(this, borderControl!!)
        }
        block.invoke(this)
    }
}