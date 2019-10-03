package net.vortexdata.tsqpf.remoteShell;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {

    public static String sha_256(String data) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedString = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < encodedString.length; i++) {
                String hex = Integer.toHexString(0xff & encodedString[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
