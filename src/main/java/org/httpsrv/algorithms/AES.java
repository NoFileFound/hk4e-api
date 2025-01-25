package org.httpsrv.algorithms;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.spec.KeySpec;
import org.httpsrv.conf.Config;

public final class AES {
    /**
     * Encrypts a string using AES.
     * @param content The given string to encrypt.
     * @return The encrypted string.
     */
    public static String encrypt(String content) {
        try {
            SecretKey secretKey = generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return BASE64.encode(cipher.doFinal(content.getBytes()));
        }catch (Exception ignored) {
            return "";
        }
    }

    /**
     * Decrypts a string using AES.
     * @param content The given string to decrypt.
     * @return The decrypted string.
     */
    public static String decrypt(String content) {
        try {
            SecretKey secretKey = generateKey();
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(BASE64.decode(content)));
        }catch (Exception e){
            return "";
        }
    }




    private static SecretKey generateKey() throws Exception {
        byte[] salt = new byte[16];
        KeySpec spec = new PBEKeySpec(Config.getPropertiesVar().privateKey.toCharArray(), salt, 65536, 128);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        SecretKey secretKey = factory.generateSecret(spec);
        return new SecretKeySpec(secretKey.getEncoded(), "AES");
    }
}