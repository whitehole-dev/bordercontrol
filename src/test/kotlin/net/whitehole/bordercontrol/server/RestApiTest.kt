package net.whitehole.bordercontrol.server;
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class RestApiTest {
    @Test
    fun testIfAlive() = testApp {
        val response = client.get("/alive")
        assertEquals(HttpStatusCode.OK, response.status, "Checks if the service is reachable under /alive which is a public test path that should return 200 OK")
    }

}