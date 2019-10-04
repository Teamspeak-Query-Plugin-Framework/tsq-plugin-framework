package net.vortexdata.tsqpf.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author Michael Wiesinger
 * @since 2.0.0
 */
public class CipherUtils {




    /**
     *  Obfuscates secret and creates a more secure keySpec.
     *
     * @param secret
     */
    public static SecretKeySpec createMessageDigest(byte[] secret) {
        try {
            byte[] key;
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            key = sha1.digest(secret);
            key = Arrays.copyOf(key, 16);
            return new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encrypts give data.
     *
     * @param data
     * @return
     */
    public static byte[] encrypt(byte[] data, SecretKeySpec secretKeySpec) {
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Decrypts give data.
     *
     * @param data
     * @return
     */
    public static byte[] decrypt(byte[] data, SecretKeySpec secretKeySpec) {
        try {

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
