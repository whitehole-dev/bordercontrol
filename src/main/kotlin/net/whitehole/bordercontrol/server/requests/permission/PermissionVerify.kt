package net.whitehole.bordercontrol.server.requests.permission

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.BorderControl
import net.whitehole.bordercontrol.models.PermissionModel
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.`in`

@Serializable
data class PermissionAccessRequest(
        @SerialName("permissions_owning")
        val permissionsOwning: List<String>,
        @SerialName("permissions_to_check")
        val permissionsToCheck: List<String> = emptyList()
)

@Serializable
data class PermissionAccessVerifyResponse(
        val mismatches: List<String>,
        val matches: List<String>
)

fun Route.permissionVerify(borderControl: BorderControl) {

    post("has_access") {
        val body = call.receiveNullable<PermissionAccessRequest>() ?: return@post call.respond(HttpStatusCode.BadRequest)
        val response = handlePermissionHasAccessRequest(borderControl.collections.standalonePermissions, body)
        call.respond(
                if (response.mismatches.isEmpty())
                    HttpStatusCode.OK
                else
                    HttpStatusCode.Unauthorized,
                response)
    }
}

suspend fun handlePermissionHasAccessRequest(collections: CoroutineCollection<PermissionModel>, request: PermissionAccessRequest): PermissionAccessVerifyResponse {
    val permissionsToCheck = collections.find(PermissionModel::permission `in` request.permissionsToCheck).toList().distinctBy { it.permission }
    println("permissionsToCheck=" + permissionsToCheck.toList().map { it.permission })
    val validPermissions = permissionsToCheck.filter { it.unprotected || it.permission in request.permissionsOwning }.map { it.permission }
    val nonValidPermissions = permissionsToCheck.filter { it.permission !in validPermissions }.map { it.permission }
    return PermissionAccessVerifyResponse(
            matches = validPermissions,
            mismatches = nonValidPermissions
    )
}