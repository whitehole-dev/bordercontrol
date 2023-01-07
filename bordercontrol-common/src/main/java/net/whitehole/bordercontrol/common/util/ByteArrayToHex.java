package net.whitehole.bordercontrol.common.util;

public class ByteArrayToHex {
    public static byte[] toHex(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : input) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString().getBytes();
    }

    public static String toHexString(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : input) {
            result.append(String.format("%02x", aByte));
        }
        return result.toString();
    }
}
