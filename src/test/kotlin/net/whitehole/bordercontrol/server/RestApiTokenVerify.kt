package net.whitehole.bordercontrol.server

import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import net.whitehole.bordercontrol.server.requests.GetTokenVerifyResponse
import net.whitehole.bordercontrol.server.requests.PostGenerateTokenRequest
import net.whitehole.bordercontrol.server.requests.PostGenerateTokenResponse
import net.whitehole.bordercontrol.token.generateHourlyToken
import net.whitehole.bordercontrol.util.Headers
import kotlin.test.Test
import kotlin.test.assertEquals

class RestApiTokenVerify {
    @Test
    fun generateTokenAndVerify() = testApp {
        val client = createClient {
            install(ContentNegotiation) {
                json(net.whitehole.bordercontrol.json)
            }
        }
        val createResponse = client.post("token/generate") {
            setBody(PostGenerateTokenRequest("mommde@protonmail.com", permissions = listOf()))
            contentType(ContentType.Application.Json)
        }
        val token = createResponse.body<PostGenerateTokenResponse>()
        assertEquals(createResponse.status, HttpStatusCode.Created)
        println("token=${token.privateToken}")

        val verifyResponse = client.get("token/verify") {
            bearerAuth("${token.publicId}.${generateHourlyToken(randomBytes = token.privateToken.toByteArray(Charsets.US_ASCII))}")
        }
        println(verifyResponse.status)
        val response = verifyResponse.body<GetTokenVerifyResponse>()
        assertEquals(response.valid, true)
        assertEquals(verifyResponse.status, HttpStatusCode.OK)
    }
}