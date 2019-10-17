package net.vortexdata.tsqpf.remoteShell;

import net.vortexdata.tsqpf.utils.CipherUtils;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Objects;

public class CipherHelper {

    private String token;
    private SecretKeySpec keySpec;

    public CipherHelper(String token) {
        this.token = token;
        this.keySpec = CipherUtils.createMessageDigest(token.getBytes(ConnectionListener.CHARSET));
    }

    public String encryptString(String data) {
        return Base64.getEncoder().encodeToString(CipherUtils.encrypt(data.getBytes(ConnectionListener.CHARSET), keySpec));
    }

    public String decryptString(String data) {
        return new String(Objects.requireNonNull(CipherUtils.decrypt(Base64.getDecoder().decode(data), keySpec)), ConnectionListener.CHARSET);
    }

    public String decryptBytes(byte[] data) {
        return new String(Objects.requireNonNull(CipherUtils.decrypt(Base64.getDecoder().decode(data), keySpec)), ConnectionListener.CHARSET);

    }

}
