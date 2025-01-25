package org.httpsrv.algorithms;

public final class XOR {
    /**
     * Performs a xor on given array.
     * @param arr The given array.
     * @param key The given xor key.
     */
    public static void performXor(byte[] arr, byte[] key) {
        for (int i = 0; i < arr.length; i++) {
            arr[i] ^= key[i % key.length];
        }
    }
}