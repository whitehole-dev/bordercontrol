package net.whitehole.bordercontrol.server

import io.ktor.client.request.*
import io.ktor.http.*
import net.whitehole.bordercontrol.util.Headers
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class RestApiVerify {
    @Test
    fun verifyInvalidToken() = testApp {
        val response = client.get("/token/verify") {
            header(Headers.AUTHORIZATION, "Im Stupid")
        }
        assertEquals(response.status, HttpStatusCode.BadRequest)
    }

    @Test
    fun verifyEmptyToken() = testApp {
        val response = client.get("/token/verify") {
            header(Headers.AUTHORIZATION, "")
        }
        assertNotEquals(response.status, HttpStatusCode.OK)
    }

    @Test
    fun verifyNoHeaderToken() = testApp {
        val response = client.get("/token/verify")
        assertNotEquals(response.status, HttpStatusCode.OK)
    }
}