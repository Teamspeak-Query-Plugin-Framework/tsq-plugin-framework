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

package net.vortexdata.tsqpf.heartbeat;

import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsqpf.Framework;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HeartBeatListener implements Runnable {

    private static boolean running = false;
    private TS3Api api;
    private Thread thread;
    private int port;

    public HeartBeatListener(TS3Api api, int port) {
        this.api = api;
        this.port = port;
        start();
    }

    private void start() {
        if (running) return;
        thread = new Thread(this, this.getClass().getName());
        thread.start();
    }


    @Override
    public void run() {
        if (running) return;
        running = true;
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(port);
        } catch (IOException e) {
            Framework.getInstance().getLogger().printError(e.getMessage());
        }
        while (true) {
            try {

                Socket socket = listener.accept();
                JSONObject response = new JSONObject();
                response.put("type", "heartbeat");
                response.put("ping", api.getConnectionInfo().getPing());
                response.put("time", api.getConnectionInfo().getConnectedTime());
                socket.getOutputStream().write(response.toJSONString().getBytes(StandardCharsets.UTF_8));
                socket.getOutputStream().flush();
                socket.close();

            } catch (Exception e) {
                Framework.getInstance().getLogger().printError(e.getMessage());
            }
        }

    }
}
