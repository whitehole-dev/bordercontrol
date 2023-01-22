package net.whitehole.bordercontrol.util

import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter

fun addByteArrays(vararg arrays: ByteArray): ByteArray {
    val input = ByteArrayOutputStream(arrays.sumOf { it.size })
    for (array in arrays)
        input.writeBytes(array)
    return input.toByteArray()
}

operator fun ByteArray.plus(byteArray: ByteArray) = addByteArrays(this, byteArray)

fun ByteArray.toHex() = joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }.toByteArray(Charsets.US_ASCII)