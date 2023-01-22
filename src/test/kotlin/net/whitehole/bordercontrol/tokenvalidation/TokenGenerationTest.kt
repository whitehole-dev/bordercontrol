package net.whitehole.bordercontrol.tokenvalidation

import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.token.generateHourlyToken
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class TokenGenerationTest {
    @Test
    fun verifyGeneratedTokensAreSame() {
        val token = TokenModel(contact = "mommde@protonmail.com")
        println(generateHourlyToken(token.randomBytes))
        assertEquals(generateHourlyToken(token.randomBytes), generateHourlyToken(token.randomBytes), "Same in current hour")
        assertEquals(generateHourlyToken(token.randomBytes, -1.0), generateHourlyToken(token.randomBytes, -1.0), "Same in -1 hour")
        assertEquals(generateHourlyToken(token.randomBytes, -0.0), generateHourlyToken(token.randomBytes, 0.0), "Same in current hour")
    }

    @Test
    fun verifyDecimalGeneratedTokenAreSame() {
        val token = TokenModel(contact = "mommde@protonmail.com")
        assertEquals(generateHourlyToken(token.randomBytes, -5.8), generateHourlyToken(token.randomBytes, -5.8), "Same in -5.8 decimal hour")
    }

    @Test
    fun testTokenAreNotEqual() {
        val token = TokenModel(contact = "idk")
        assertNotEquals(generateHourlyToken(token.randomBytes), generateHourlyToken(token.randomBytes, -5.2), "Token are not equal")
    }
}