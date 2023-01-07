package net.whitehole.bordercontrol.common.models;

import net.whitehole.bordercontrol.common.util.ByteArrayToHex;
import net.whitehole.bordercontrol.common.util.NumberToByteArrayConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

import static net.whitehole.bordercontrol.common.util.ByteArrayToHex.toHex;
import static net.whitehole.bordercontrol.common.util.ByteArrayToHex.toHexString;

/**
 * A Border Token model for that is required for computing a valid Bearer Token
 * @param randomBytes 64 bytes 2^(64*8) possibilities
 * @param timestamp when this token was created
 * @param publicId id used for authenticating with this token
 */
public record BorderTokenModel(
        byte[] randomBytes,
        long timestamp,
        byte[] publicId
) {
    private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!/?.:;'\"&*()_+{}";
    public BorderTokenModel() {
        this(NumberToByteArrayConverter.longToByteArray(new Random().nextLong()), System.currentTimeMillis(), NumberToByteArrayConverter.longToByteArray(new Random().nextLong()));
    }

    /**
     * This will make a new Token based on the time you are invoking this method. (UNIX TIME)
     * To avoid complication when the hour changes (e.g. the client makes an request a few milliseconds before the hour changes, so when it arrives on the server, the token is already invalid),
     * the server checks 30 seconds in the new hour also the old token.
     */

    public String generateTimebasedAuthenticationToken() {
        return toHexString(generateRandomString(64, new SecureRandom((generateTimebasedToken() + randomBytes).getBytes())).getBytes(StandardCharsets.UTF_8));
    }
    public String generateTimebasedToken() {
        return new String(toHex(generateRandomString(64, new Random(generateHourlyTimestamp())).getBytes(Charset.defaultCharset())));
    }
    public long generateHourlyTimestamp(int relativeHour) {
        return generateHourlyTimestamp() +  ((long) relativeHour * 1000 * 60 * 60);
    }
    public long generateHourlyTimestamp() {
        return (System.currentTimeMillis() / 1000 / 60 / 60) * 1000 * 60 * 60;
    }

    private String generateRandomString(int length, Random random) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < length; i++) {
            s.append(SYMBOLS.toCharArray()[random.nextInt(0, SYMBOLS.length() - 1)]);
        }
        return s.toString();
    }

}
