package net.whitehole.bordercontrol.token

import net.whitehole.bordercontrol.models.ContextualPermissionModel
import net.whitehole.bordercontrol.models.PermissionModel
import net.whitehole.bordercontrol.models.TokenModel
import org.litote.kmongo.coroutine.CoroutineCollection
import org.litote.kmongo.eq
import kotlin.time.Duration

suspend fun TokenModel.getContextualPermission(permission: String, collection: CoroutineCollection<PermissionModel>, duration: Duration = Duration.INFINITE): ContextualPermissionModel {
    val permission = permission.lowercase()
    val standaloneModel = collection.findOne(PermissionModel::permission eq permission) ?: error("No permission with name = $permission")
    return ContextualPermissionModel(permission = permission, parent = standaloneModel.id, timeValid = duration, expired = duration.isNegative())
}