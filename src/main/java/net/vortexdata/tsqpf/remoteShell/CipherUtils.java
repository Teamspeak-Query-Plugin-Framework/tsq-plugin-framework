package net.vortexdata.tsqpf.remoteShell;

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

    private static SecretKeySpec secretKeySpec;
    private static byte[] key;

    /**
     *  Obfuscates secret and creates a more secure keySpec.
     *
     * @param secret
     */
    public static void createMessageDigest(byte[] secret) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            key = sha1.digest(secret);
            key = Arrays.copyOf(key, 16);
            secretKeySpec = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Encrypts give data.
     *
     * @param data
     * @param secret
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] secret) {
        try {
            createMessageDigest(secret);
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
     * @param secret
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] secret) {
        try {
            createMessageDigest(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return cipher.doFinal(data);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }

}
