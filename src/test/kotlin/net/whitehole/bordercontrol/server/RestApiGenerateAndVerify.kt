package net.whitehole.bordercontrol.server

import com.mongodb.client.model.InsertManyOptions
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import net.whitehole.bordercontrol.models.PermissionModel
import net.whitehole.bordercontrol.server.requests.PostGenerateTokenRequest
import net.whitehole.bordercontrol.server.requests.PostGenerateTokenResponse
import net.whitehole.bordercontrol.server.requests.permission.PermissionAccessVerifyResponse
import net.whitehole.bordercontrol.setup.borderControl
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

class RestApiGenerateAndVerify {

    @Test
    fun generateTokenWithoutAnyPermissions() = testApp {
        val client = createClient {
            install(ContentNegotiation) {
                json(net.whitehole.bordercontrol.json)
            }
        }
        val response = client.post("token/generate") {
            setBody(PostGenerateTokenRequest("mommde@protonmail.com"))
            contentType(ContentType.Application.Json)
        }
        val body = response.body<PostGenerateTokenResponse>()
        assertEquals(response.status, HttpStatusCode.Created, "token was generated successfully")
        assertEquals(body.permissions, emptyList(), "That the token has no permissions")
        println("public_id=${body.publicId}; ascii_private_token=${body.privateToken}")
    }


    @Test
    fun generateTokenWithUnregisteredPermissions() = testApp {
        val client = createClient {
            install(ContentNegotiation) {
                json(net.whitehole.bordercontrol.json)
            }
        }
        val response = client.post("token/generate") {
            setBody(PostGenerateTokenRequest("mommde@protonmail.com", permissions = listOf("asdf",
                    "sdfgjbnbxcvn",
                    "jasfldghdsfgyioghkjsdfhg")))
            contentType(ContentType.Application.Json)
        }
        val body = response.body<PostGenerateTokenResponse>()
        assertEquals(response.status, HttpStatusCode.Created, "token was generated successfully")
        assertEquals(body.permissions, emptyList(), "That the token has no permissions")
        println("public_id=${body.publicId}; ascii_private_token=${body.privateToken}; permissions=${body.permissions.joinToString(",")}")
    }

    @Test
    fun generateTokenWithRegisteredPermissions() = testApp {
        borderControl!!.collections.standalonePermissions.insertOne(PermissionModel(
                permission = "hello.tests",
                unprotected = true
        ))
        val client = createClient {
            install(ContentNegotiation) {
                json(net.whitehole.bordercontrol.json)
            }
        }
        val response = client.post("token/generate") {
            setBody(PostGenerateTokenRequest("mommde@protonmail.com", permissions = listOf("hello.tests",
                    "sdfgjbnbxcvn",
                    "jasfldghdsfgyioghkjsdfhg")))
            contentType(ContentType.Application.Json)
        }
        println(response.status)
        println(response.bodyAsText())
        val body = response.body<PostGenerateTokenResponse>()
        assertEquals(response.status, HttpStatusCode.Created, "token was generated successfully")
        assertEquals(body.permissions, listOf("hello.tests"), "That the token has no permissions")
        println("public_id=${body.publicId}; ascii_private_token=${body.privateToken}; permissions=${body.permissions.joinToString(",")}")
    }

    @Test
    fun generateTokenWithRegisteredPermissionsProtected() = testApp {
        borderControl!!.collections.standalonePermissions.insertMany(listOf(PermissionModel(
                permission = "hello.secure.tests",
        ), PermissionModel(permission = "hello.tests", unprotected = true)), InsertManyOptions())
        val client = createClient {
            install(ContentNegotiation) {
                json(net.whitehole.bordercontrol.json)
            }
        }
        val response = client.post("token/generate") {
            setBody(PostGenerateTokenRequest("mommde@protonmail.com", permissions = listOf("hello.tests",
                    "hello.secure.tests",
                    "sdfgjbnbxcvn",
                    "jasfldghdsfgyioghkjsdfhg")))
            contentType(ContentType.Application.Json)
        }
        println(response.status)
        println(response.bodyAsText())
        val body = response.body<PermissionAccessVerifyResponse>()
        assertEquals(response.status, HttpStatusCode.Unauthorized, "token was successfully denied generation")
        assertEquals(body.matches, listOf("hello.tests"), "That the token has permission")
        assertEquals(body.mismatches, listOf("hello.secure.tests"), "That the secured permission was not granted")
    }
}