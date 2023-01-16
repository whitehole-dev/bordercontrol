package net.whitehole.bordercontrol.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.serializer.DurationSerializer
import net.whitehole.bordercontrol.serializer.UUIDSerializer
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.UUID
import kotlin.time.Duration

enum class PermissionRequestAction {
    APPEND,
    REMOVE
}

@Serializable
data class TokenPermissionRequest(
        val id: Id<TokenPermissionRequest> = newId(),
        val permission: String,
        @Serializable(DurationSerializer::class)
        val duration: Duration = Duration.INFINITE,
        @Serializable(UUIDSerializer::class)
        val token: UUID,
        val action: PermissionRequestAction
)