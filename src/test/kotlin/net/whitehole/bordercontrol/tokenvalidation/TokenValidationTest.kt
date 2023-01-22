package net.whitehole.bordercontrol.tokenvalidation

import net.whitehole.bordercontrol.token.isFirst30SecondsInNewHour
import java.util.Calendar
import kotlin.test.Test
import kotlin.test.assertEquals

class TokenValidationTest {

    @Test
    fun testIfWeAreInTheFirst30SecondsOfAHour() {
        assertEquals(isFirst30SecondsInNewHour(), Calendar.getInstance().get(Calendar.SECOND) <= 30)
    }
}