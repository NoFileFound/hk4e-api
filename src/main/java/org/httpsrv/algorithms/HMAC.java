package org.httpsrv.algorithms;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class HMAC {
    /**
     * Creates a new HMAC hash by given content and key.
     * @param content The given data.
     * @param key The given key.
     * @return A HMAC hash.
     */
    public static String createHMACHash(String content, String key) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKeySpec);
        return HEX.bytesToHex(mac.doFinal(content.getBytes()));
    }
}