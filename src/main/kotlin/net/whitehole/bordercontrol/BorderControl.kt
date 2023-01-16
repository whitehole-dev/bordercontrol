package net.whitehole.bordercontrol

import mu.KotlinLogging
import net.whitehole.bordercontrol.config.Config
import net.whitehole.bordercontrol.models.ContextualPermissionModel
import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.server.RestApi
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


class BorderControl(private val backendConnectionString: String? = null) {
    private val restApi = RestApi(this)
    private val logger = KotlinLogging.logger {}
    val config = Config()
    //                                                  used for testing
    private val client = KMongo.createClient(backendConnectionString ?: config.MONGO_URI).coroutine
    val database = client.getDatabase("border_control")
    val collections = Collections(this)

    class Collections(borderControl: BorderControl) {
        val tokens = borderControl.getDatabase<TokenModel>("tokens")
        val permissions = borderControl.getDatabase<ContextualPermissionModel>("permission_requests")
    }
    init {
        if (backendConnectionString == null)
            restApi.launch()
    }

    inline fun <reified T : Any> getDatabase(name: String) = database.getCollection<T>(name)
}

fun main() {
    println("Starting Bordercontrol")
    BorderControl()
}