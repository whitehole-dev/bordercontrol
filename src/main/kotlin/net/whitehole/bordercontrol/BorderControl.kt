package net.whitehole.bordercontrol

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import io.ktor.client.*
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import net.whitehole.bordercontrol.config.Config
import net.whitehole.bordercontrol.generated.BuildConfig
import net.whitehole.bordercontrol.models.ContextualPermissionModel
import net.whitehole.bordercontrol.models.PermissionModel
import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.server.RestApi
import org.bson.UuidRepresentation
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

class BorderControl(private val backendConnectionString: String? = null) {
    private val logger = KotlinLogging.logger {}
    val config = Config()
    //                                                  used for testing
    private val client = KMongo.createClient(MongoClientSettings.builder()
            .applicationName("Border Control")
            .applyConnectionString(ConnectionString(backendConnectionString ?: config.MONGO_URI))
            .applyToServerSettings {

            }
            .applyToSocketSettings {

            }
            .applyToConnectionPoolSettings {

            }
            .build()).coroutine
    val database = client.getDatabase("border_control")
    val collections = Collections(this)
    val internalHttpClient = HttpClient(CIO) {

        install(ContentNegotiation) {
            json(json)
        }

        defaultRequest {
            url("http://$host:$port/")
            userAgent("Bordercontrol / ${BuildConfig.VERSION}(${BuildConfig.GIT_HASH})")
        }
    }
    val restApi = RestApi(this)

    class Collections(borderControl: BorderControl) {
        val tokens = borderControl.getDatabase<TokenModel>("tokens")
        val contextualPermissions = borderControl.getDatabase<ContextualPermissionModel>("contextual_permission")
        val standalonePermissions = borderControl.getDatabase<PermissionModel>("standalone_permission")
    }
    init {
        if (backendConnectionString == null)
            restApi.server.start(wait = true)
    }

    inline fun <reified T : Any> getDatabase(name: String) = database.getCollection<T>(name)
}

fun main() {
    println("Starting Bordercontrol")
    BorderControl()
}