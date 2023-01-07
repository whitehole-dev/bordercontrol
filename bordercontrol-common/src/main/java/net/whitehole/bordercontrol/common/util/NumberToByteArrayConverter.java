package net.whitehole.bordercontrol.common.util;

public class NumberToByteArrayConverter {
    public static byte[] intToByteArray(int n) {
        return new byte[] {
                (byte)(n >>> 24),
                (byte)(n >>> 16),
                (byte)(n >>> 8),
                (byte) n
        };
    }

    public static byte[] longToByteArray(long n) {
        return new byte[] {
                (byte)(n >>> 8*7),
                (byte)(n >>> 8*6),
                (byte)(n >>> 8*5),
                (byte)(n >>> 8*4),
                (byte)(n >>> 8*3),
                (byte)(n >>> 8*2),
                (byte)(n >>> 8),
                (byte) n
        };
    }
}
