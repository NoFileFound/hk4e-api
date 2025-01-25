package org.httpsrv.algorithms;

public final class RC4 {
    /**
     * Encodes a string to RC4 one.
     * @param input The given string to encode.
     * @param key The given key.
     * @return Encoded string.
     */
    public static String encode(String input, String key) {
        byte[] keyBytes = key.getBytes();
        byte[] inputBytes = input.getBytes();
        byte[] encryptedBytes = processRC4(keyBytes, inputBytes);
        return BASE64.encode(encryptedBytes);
    }

    /**
     * Decodes a RC4 string.
     * @param input The given string to decode.
     * @param key The given key.
     * @return Decoded string.
     */
    public static String decode(String input, String key) {
        byte[] keyBytes = key.getBytes();
        byte[] inputBytes = BASE64.decode(input.replace("\n", ""));
        byte[] decryptedBytes = processRC4(keyBytes, inputBytes);
        return new String(decryptedBytes);
    }




    private static byte[] processRC4(byte[] key, byte[] input) {
        byte[] result = new byte[input.length];
        byte[] s = new byte[256];
        int j = 0;

        for (int i = 0; i < 256; i++) {
            s[i] = (byte)i;
        }
        for (int i = 0; i < 256; i++) {
            j = (j + s[i] + key[i % key.length]) & 0xFF;
            byte temp = s[i];
            s[i] = s[j];
            s[j] = temp;
        }

        int i = 0;
        j = 0;
        for (int y = 0; y < input.length; y++) {
            i = (i + 1) & 0xFF;
            j = (j + s[i]) & 0xFF;
            byte temp = s[i];
            s[i] = s[j];
            s[j] = temp;
            byte k = s[(s[i] + s[j]) & 0xFF];
            result[y] = (byte) (input[y] ^ k);
        }

        return result;
    }
}