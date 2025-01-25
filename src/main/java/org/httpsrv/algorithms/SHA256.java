package org.httpsrv.algorithms;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class SHA256 {
    /**
     * Hashes a given string.
     * @param content The given string.
     * @return The sha256 hash of it.
     */
    public static String createHash(String content) throws NoSuchAlgorithmException {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        final byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));

        final StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
    }

    /**
     * Compares a string and hash.
     * @param content The given string.
     * @param hash The given hash.
     * @return Checks if they are equal.
     */
    public static boolean compareString(String content, String hash) {
        try {
            return createHash(content).equals(hash);
        }
        catch (NoSuchAlgorithmException e) {
            return false;
        }
    }
}