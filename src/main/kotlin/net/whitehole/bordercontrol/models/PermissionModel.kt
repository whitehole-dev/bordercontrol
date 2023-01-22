package net.whitehole.bordercontrol.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.serializer.UUIDSerializer
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.UUID

@Serializable
data class PermissionModel(
        val id: Id<PermissionModel> = newId(),
        @SerialName("owner")
        @Serializable(UUIDSerializer::class)
        // token of the owner public
        val owner: UUID = UUID.randomUUID(),
        val permission: String,
        val unprotected: Boolean = false,
        val timestamp: Long = System.currentTimeMillis()
)
