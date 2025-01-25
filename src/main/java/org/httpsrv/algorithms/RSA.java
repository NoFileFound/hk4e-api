package org.httpsrv.algorithms;

import static org.httpsrv.utils.Utils.readResourceFile;
import lombok.Getter;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import org.httpsrv.utils.Jackson;

public final class RSA {
    private static byte[] passwordDecryptionKey;
    private static PrivateKey dispatchSignatureKey;
    private static final Map<Integer, PublicKey> EncryptionKeys = new HashMap<>();
    @Getter private static byte[] dispatchSeed;
    @Getter private static byte[] dispatchKey;

    /**
     * Initialize the encryption.
     */
    public static void loadEncryption() {
        // password decryption
        passwordDecryptionKey = readResourceFile("crypto/passwordPriv.bin");

        // dispatch
        dispatchSeed = readResourceFile("crypto/dispatch/dispatchSeed.bin");
        dispatchKey = readResourceFile("crypto/dispatch/dispatchKey.bin");

        // dispatch keys
        try {
            dispatchSignatureKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Objects.requireNonNull(readResourceFile("crypto/SigningKey.der"))));

            Map<Integer, String> keys = new HashMap<>(Map.of(
                    2, "crypto/keys/2.der",
                    3, "crypto/keys/3.der",
                    4, "crypto/keys/4.der",
                    5, "crypto/keys/5.der"

            ));

            for (Map.Entry<Integer, String> entry : keys.entrySet()) {
                PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Objects.requireNonNull(readResourceFile(entry.getValue()))));
                EncryptionKeys.put(entry.getKey(), publicKey);
            }

        } catch (Exception ignored) {

        }
    }

    /**
     * Decrypts the given password (for login in the game).
     *
     * @param password The given password to decrypt.
     * @return The decrypted password.
     * @throws BadPaddingException Decryption key is invalid.
     */
    public static String DecryptPassword(String password) throws Exception {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(passwordDecryptionKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPrivateKey private_key = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, private_key);
        return new String(cipher.doFinal(BASE64.decode(password)), java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * Signs the region.
     *
     * @param regionInfo Region data.
     * @param key_id     The platform.
     * @return Signature and encrypted content.
     */
    public static String encryptAndSignRegionData(byte[] regionInfo, Integer key_id) throws Exception {
        if (key_id == null) {
            throw new Exception("Key ID was not set");
        }

        ByteArrayOutputStream encryptedRegionInfoStream = new ByteArrayOutputStream();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, EncryptionKeys.get(key_id));

        int chunkSize = 256 - 11;
        int regionInfoLength = regionInfo.length;
        int numChunks = (int) Math.ceil(regionInfoLength / (double) chunkSize);
        for (int i = 0; i < numChunks; i++) {
            byte[] chunk = Arrays.copyOfRange(regionInfo, i * chunkSize, Math.min((i + 1) * chunkSize, regionInfoLength));
            byte[] encryptedChunk = cipher.doFinal(chunk);
            encryptedRegionInfoStream.write(encryptedChunk);
        }

        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(dispatchSignatureKey);
        privateSignature.update(regionInfo);

        LinkedHashMap<String, String> data = new LinkedHashMap<>();
        data.put("content", BASE64.encode(encryptedRegionInfoStream.toByteArray()));
        data.put("sign", BASE64.encode(privateSignature.sign()));
        return Jackson.toJsonString(data);
    }
}