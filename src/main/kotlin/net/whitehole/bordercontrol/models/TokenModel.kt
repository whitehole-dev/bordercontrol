package net.whitehole.bordercontrol.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.whitehole.bordercontrol.serializer.UUIDSerializer
import net.whitehole.bordercontrol.util.toHex
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import java.util.UUID
import kotlin.random.Random

@Serializable
data class TokenModel(
        val id: Id<TokenModel> = newId(),
        // 2^(20*8) possibilities
        @SerialName("random_bytes")
        val randomBytes: ByteArray = Random.nextBytes(20).toHex(),
        @SerialName("time_created")
        val timeCreated: Long = System.currentTimeMillis(),
        @Serializable(UUIDSerializer::class)
        val publicId: UUID = UUID.randomUUID(),
        val contact: String,
        var permissions: List<Id<ContextualPermissionModel>> = emptyList()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TokenModel

        if (id != other.id) return false
        if (!randomBytes.contentEquals(other.randomBytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + randomBytes.contentHashCode()
        return result
    }
}
