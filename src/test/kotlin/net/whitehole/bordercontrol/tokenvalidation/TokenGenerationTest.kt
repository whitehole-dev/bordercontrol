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
        assertEquals(token.generateHourlyToken(), token.generateHourlyToken(), "Same in current hour")
        assertEquals(token.generateHourlyToken(-1.0), token.generateHourlyToken(-1.0), "Same in -1 hour")
        assertEquals(token.generateHourlyToken(-0.0), token.generateHourlyToken(0.0), "Same in current hour")
    }

    @Test
    fun verifyDecimalGeneratedTokenAreSame() {
        val token = TokenModel(contact = "mommde@protonmail.com")
        assertEquals(token.generateHourlyToken(-5.8), token.generateHourlyToken(-5.8), "Same in -5.8 decimal hour")
    }

    @Test
    fun testTokenAreNotEqual() {
        val token = TokenModel(contact = "idk")
        assertNotEquals(token.generateHourlyToken(), token.generateHourlyToken(-5.2), "Token are not equal")
    }
}