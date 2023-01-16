package net.whitehole.bordercontrol.token

import net.whitehole.bordercontrol.models.ContextualPermissionModel

fun ContextualPermissionModel.isExpired(): Boolean {
    return expired || timestamp + timeValid.inWholeMilliseconds > System.currentTimeMillis()
}