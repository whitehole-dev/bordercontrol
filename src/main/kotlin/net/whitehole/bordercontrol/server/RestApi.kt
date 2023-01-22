package net.whitehole.bordercontrol.server

import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import mu.KotlinLogging
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.json
import net.whitehole.bordercontrol.server.requests.httpGenerate
import net.whitehole.bordercontrol.server.requests.httpVerify
import net.whitehole.bordercontrol.server.requests.permission.permissionVerify

class RestApi(private val borderControl: BorderControl) {
    lateinit var server: CIOApplicationEngine
    companion object {
        val module: Application.(BorderControl) -> Unit = { borderControl ->

            install(ContentNegotiation) {
                json(json)
            }

            routing {
                get("alive") {
                    call.respond(HttpStatusCode.OK)
                }

                route("token") {
                    httpVerify(borderControl)
                    httpGenerate(borderControl)
                }

                route("permission") {
                    permissionVerify(borderControl)
                }
            }
        }
    }
    init {
        server = embeddedServer(CIO, applicationEngineEnvironment {
            connector {
                host = "0.0.0.0"
                port = 8080
            }

            developmentMode = borderControl.config.DEBUG

            log = KotlinLogging.logger{}

            module {
                module.invoke(this, borderControl)
            }
        })
    }
}