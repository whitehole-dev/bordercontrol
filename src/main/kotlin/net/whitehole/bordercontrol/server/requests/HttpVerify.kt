package net.whitehole.bordercontrol.server.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.charsets.*
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.token.generateHourlyToken
import net.whitehole.bordercontrol.token.isFirst30SecondsInNewHour
import net.whitehole.bordercontrol.util.Headers
import org.litote.kmongo.eq
import java.util.UUID

fun Route.httpVerify(borderControl: BorderControl) {
    get("/verify") {
        var authHeader = call.request.headers[Headers.AUTHORIZATION]
                ?: return@get call.respond(HttpStatusCode.BadRequest)

        authHeader = authHeader.replace("Bearer ", "")
        val tokenHeaders = authHeader.split(".")
        if (tokenHeaders.isEmpty() || tokenHeaders.size == 1)
            return@get call.respond(HttpStatusCode.BadRequest)
        val publicId = tokenHeaders[0]
        val authToken = tokenHeaders[1]

        val permissions = call.request.headers[Headers.PERMISSIONS]?.split(";")?.map { it.lowercase() } ?: listOf()

        val token = borderControl.collections.tokens.findOne(TokenModel::publicId eq UUID.fromString(publicId))
                ?: return@get call.respond(HttpStatusCode.NotFound)

        val currentTimestamp = token.generateHourlyToken()
        if (!authToken.contentEquals(currentTimestamp))
            if (isFirst30SecondsInNewHour() && (!authToken.contentEquals(token.generateHourlyToken(-1.0))))
                return@get call.respond(HttpStatusCode.Unauthorized)

        val valid = token.permissions.map { it.permission }.containsAll(permissions)
        val map = mutableMapOf(
                "exists" to true,
                "valid" to valid,
                "mismatches" to permissions.filter { token.permissions.map { context -> context.permission }.contains(it) }
        )

        call.respond(if (valid) HttpStatusCode.OK else HttpStatusCode.Unauthorized, map)
    }
}