package net.whitehole.bordercontrol.token

import dev.schlaubi.stdx.core.hash
import net.whitehole.bordercontrol.models.TokenModel
import net.whitehole.bordercontrol.util.addByteArrays
import java.nio.charset.Charset
import java.security.SecureRandom
import java.util.Calendar
import kotlin.random.Random

fun generateHourlyToken(randomBytes: ByteArray, relative: Double = 0.0): String {
    var hour = ((System.currentTimeMillis() / 1000 / 60 / 60) * 1000 * 60 * 60)
    hour += (relative * 1000 * 60 * 60).toLong()
    return (hour.toString().toByteArray(Charsets.US_ASCII) + randomBytes).hash("SHA256")
}

fun isFirst30SecondsInNewHour() = Calendar.getInstance().get(Calendar.SECOND) <= 30