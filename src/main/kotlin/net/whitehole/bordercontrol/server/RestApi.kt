package net.whitehole.bordercontrol.server

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import mu.KotlinLogging
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.server.requests.httpVerify

class RestApi(private val borderControl: BorderControl) {
    companion object {
        val module: Application.(BorderControl) -> Unit = { borderControl ->
            routing {
                get("/alive") {
                    call.respond(HttpStatusCode.OK)
                }

                httpVerify(borderControl)

                get("/about") {

                }
            }
        }
    }
    fun launch() {
        embeddedServer(CIO, applicationEngineEnvironment {
            connector {
                host = "0.0.0.0"
                port = 8080
            }

            developmentMode = borderControl.config.DEBUG

            log = KotlinLogging.logger{}

            module {
                module.invoke(this, borderControl)
            }

        }).start(wait = true)
    }
}