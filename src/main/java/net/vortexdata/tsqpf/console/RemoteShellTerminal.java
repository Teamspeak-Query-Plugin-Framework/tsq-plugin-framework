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

package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.remoteShell.CipherHelper;
import net.vortexdata.tsqpf.remoteShell.ConnectionListener;
import net.vortexdata.tsqpf.remoteShell.Session;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * <p>RemoteShellTerminal class.</p>
 *
 * @author TAXSET
 * @version $Id: $Id
 */
public class RemoteShellTerminal implements VirtualTerminal {

    private InputStream inputStream;
    private OutputStream outputStream;
    private CipherHelper cipherHelper;
    private Session session;


    /**
     * <p>Constructor for RemoteShellTerminal.</p>
     *
     * @param inputStream a {@link java.io.InputStream} object.
     * @param outputStream a {@link java.io.OutputStream} object.
     * @param helper a {@link net.vortexdata.tsqpf.remoteShell.CipherHelper} object.
     * @param session a {@link net.vortexdata.tsqpf.remoteShell.Session} object.
     */
    public RemoteShellTerminal(InputStream inputStream, OutputStream outputStream, CipherHelper helper, Session session) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.cipherHelper = helper;
        this.session = session;
    }



    /** {@inheritDoc} */
    @Override
    public void println(String msg) {
        try {
            JSONObject container = new JSONObject();
            JSONObject data = new JSONObject();

            data.put("type", "terminal");
            data.put("mode", "println");
            data.put("message", msg);

            container.put("type", "data");
            container.put("data", cipherHelper.encryptString(data.toJSONString()));
            outputStream.write(container.toJSONString().getBytes(ConnectionListener.CHARSET));
            outputStream.write(ConnectionListener.END_OF_MESSAGE);
            outputStream.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void print(String msg) {

        println(msg);
    }




    /** {@inheritDoc} */
    @Override
    public String readln() {
        try {
            JSONObject data = new JSONObject();
            String readId = UUID.randomUUID().toString();
            data.put("type", "terminal");
            data.put("mode", "readln");
            data.put("id", readId);
            outputStream.write(data.toJSONString().getBytes(ConnectionListener.CHARSET));
            outputStream.write(ConnectionListener.END_OF_MESSAGE);
            outputStream.flush();

            while (true) {
                JSONObject response = session.terminalUserInputBuffer.take();

                if (!response.get("id").equals(readId)) continue;
                return (String) response.get("input");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public void logout() {
        session.shutdown();
    }
}
