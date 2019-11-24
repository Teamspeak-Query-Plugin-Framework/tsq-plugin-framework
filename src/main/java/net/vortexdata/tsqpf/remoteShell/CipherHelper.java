/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

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
