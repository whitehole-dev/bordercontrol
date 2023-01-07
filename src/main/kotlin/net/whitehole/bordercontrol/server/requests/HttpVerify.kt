package net.whitehole.bordercontrol.server.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.common.util.Headers

fun Route.httpVerify(borderControl: BorderControl) {
    get("/verify") {
        val authHeader = call.request.headers[Headers.AUTHORIZATION]?.replace("Bearer ", "")
                ?: return@get call.respond(HttpStatusCode.BadRequest)
        val permissions = call.request.headers[Headers.PERMISSIONS]?.split(";") ?: listOf()


        call.respond(mapOf(
                "exists" to ""
        ))
    }
}