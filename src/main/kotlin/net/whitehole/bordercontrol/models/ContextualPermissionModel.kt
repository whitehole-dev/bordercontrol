package net.whitehole.bordercontrol.models

import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.serializer.DurationSerializer
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import kotlin.time.Duration

@Serializable
data class ContextualPermissionModel(
        val id: Id<ContextualPermissionModel> = newId(),
        val permission: String,
        val parent: Id<PermissionModel>,
        val expired: Boolean = false,
        @Serializable(DurationSerializer::class)
        val timeValid: Duration =  Duration.INFINITE,
        val timestamp: Long = System.currentTimeMillis()
)
