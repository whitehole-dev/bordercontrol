package net.whitehole.bordercontrol.server.requests

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.models.ContextualPermissionModel
import net.whitehole.bordercontrol.models.PermissionModel
import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.serializer.UUIDSerializer
import net.whitehole.bordercontrol.server.requests.permission.PermissionAccessRequest
import net.whitehole.bordercontrol.server.requests.permission.PermissionAccessVerifyResponse
import net.whitehole.bordercontrol.server.requests.permission.handlePermissionHasAccessRequest
import net.whitehole.bordercontrol.token.getContextualPermission
import org.litote.kmongo.`in`
import java.util.UUID
import kotlin.time.Duration

@Serializable
data class PostGenerateTokenRequest(
        val contact: String,
        val permissions: List<String> = listOf()
)

@Serializable
data class PostGenerateTokenResponse(
        val permissions: List<String>,
        @Serializable(UUIDSerializer::class)
        @SerialName("public_id")
        val publicId: UUID,
        @SerialName("ascii_private_token")
        val privateToken: String
)

fun Route.httpGenerate(borderControl: BorderControl) {
    post("generate") {
        val tokenRequestBody = call.receiveNullable<PostGenerateTokenRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)

        val arePermissionsAllowed = handlePermissionHasAccessRequest(borderControl.collections.standalonePermissions,
                PermissionAccessRequest(permissionsOwning = emptyList(), permissionsToCheck = tokenRequestBody.permissions.map { it.lowercase() }.distinct()))

        if (arePermissionsAllowed.mismatches.isNotEmpty())
            return@post call.respond(HttpStatusCode.Unauthorized, arePermissionsAllowed)

        val token = TokenModel(contact = tokenRequestBody.contact)
        val contextualPermissions = arePermissionsAllowed.matches.map { token.getContextualPermission(it, borderControl.collections.standalonePermissions) }
        println(contextualPermissions.map { it.permission })
        token.permissions = contextualPermissions.map { it.id }

        borderControl.collections.tokens.insertOne(token)

        // only if there are permissions requested
        if (contextualPermissions.isNotEmpty())
            borderControl.collections.contextualPermissions.insertMany(contextualPermissions)

        call.respond(HttpStatusCode.Created, PostGenerateTokenResponse(contextualPermissions.map { it.permission }, UUID.fromString(token.publicId), token.randomBytes.toString(Charsets.US_ASCII)))

    }
}