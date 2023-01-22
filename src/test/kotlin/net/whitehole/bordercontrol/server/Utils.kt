package net.whitehole.bordercontrol.server

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import net.whitehole.bordercontrol.json
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