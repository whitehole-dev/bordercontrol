package net.whitehole.bordercontrol.server.requests

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.models.ContextualPermissionModel
import net.whitehole.bordercontrol.models.PermissionModel
import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.server.requests.permission.PermissionAccessRequest
import net.whitehole.bordercontrol.server.requests.permission.handlePermissionHasAccessRequest
import net.whitehole.bordercontrol.token.generateHourlyToken
import net.whitehole.bordercontrol.token.isFirst30SecondsInNewHour
import net.whitehole.bordercontrol.util.Headers
import org.litote.kmongo.coroutine.aggregate
import org.litote.kmongo.eq
import org.litote.kmongo.`in`
import org.litote.kmongo.lt
import org.litote.kmongo.min
import java.util.UUID


@Serializable
data class GetTokenVerifyResponse(
        val valid: Boolean = false,
        val mismatches: List<String>
)
fun Route.httpVerify(borderControl: BorderControl) {
    get("verify") {
        var authHeader = call.request.headers[Headers.AUTHORIZATION]
                ?: return@get call.respond(HttpStatusCode.BadRequest)

        authHeader = authHeader.replace("Bearer ", "")
        val tokenHeaders = authHeader.split(".")

        if (tokenHeaders.isEmpty() || tokenHeaders.size == 1)
            return@get call.respond(HttpStatusCode.BadRequest)

        val publicId = tokenHeaders[0]
        val authToken = tokenHeaders[1]

        val permissions = call.request.headers[Headers.PERMISSIONS]?.split(";")?.map { it.lowercase() } ?: listOf()

        val token = borderControl.collections.tokens.findOne(TokenModel::publicId eq publicId)
                ?: return@get call.respond(HttpStatusCode.NotFound)

        val currentTimestamp = generateHourlyToken(token.randomBytes)
        if (!authToken.contentEquals(currentTimestamp))
            if (isFirst30SecondsInNewHour() && (!authToken.contentEquals(generateHourlyToken(token.randomBytes, -1.0))))
                return@get call.respond(HttpStatusCode.Unauthorized)

        val hasAccess = handlePermissionHasAccessRequest(borderControl.collections.standalonePermissions, PermissionAccessRequest(
                permissionsOwning = borderControl.collections.contextualPermissions.find(ContextualPermissionModel::id `in` token.permissions).toList().map { it.permission },
                permissionsToCheck = permissions
        ))


        call.respond(if (hasAccess.mismatches.isEmpty()) HttpStatusCode.OK else HttpStatusCode.Unauthorized, GetTokenVerifyResponse(
                valid = true,
                mismatches = hasAccess.mismatches
        ))
    }
}