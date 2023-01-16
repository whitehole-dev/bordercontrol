package net.whitehole.bordercontrol.models

import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.serializer.DurationSerializer
import org.litote.kmongo.Id
import kotlin.time.Duration

@Serializable
data class ContextualPermissionModel(
        val id: Id<ContextualPermissionModel>,
        val permission: String,
        val parent: Id<PermissionModel>,
        val expired: Boolean = false,
        @Serializable(DurationSerializer::class)
        val timeValid: Duration,
        val timestamp: Long
)
