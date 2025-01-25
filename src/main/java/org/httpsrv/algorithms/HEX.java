package org.httpsrv.algorithms;

public final class HEX {
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(java.nio.charset.StandardCharsets.US_ASCII);

    /**
     * Converts bytes to hex.
     * @param bytes The given bytes.
     * @return HEX array.
     */
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Converts hex array to bytes.
     * @param hex The given hex array.
     * @return The bytes.
     */
    public static byte[] hexToBytes(String hex) {
        int length = hex.length();
        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4) + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
}