package org.httpsrv.algorithms;

import java.util.Base64;
public final class BASE64 {
    /**
     * Encodes the given bytes of string into a Base64.
     *
     * @param content The bytes to encode.
     * @return A base64 string.
     */
    public static String encode(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }

    /**
     * Decodes the given Base64 string into normal one.
     *
     * @param content The base64 string to decode.
     * @return A decoded string.
     */
    public static byte[] decode(String content) {
        return Base64.getDecoder().decode(content);
    }
}