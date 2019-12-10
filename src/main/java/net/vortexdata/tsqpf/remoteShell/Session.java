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


import net.vortexdata.tsqpf.*;
import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.RemoteShellTerminal;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;
import net.vortexdata.tsqpf.utils.HashUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Session implements Runnable {

    public BlockingQueue<JSONObject> terminalUserInputBuffer;
    private String id;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread thread;
    private ConnectionListener listener;
    private User user = null;
    private String token = null;
    private RemoteShellTerminal terminal;
    private CipherHelper cipherHelper;
    private Logger logger;
    private FrameworkContainer frameworkContainer;

    public Session(FrameworkContainer frameworkContainer, String id, Socket socket, InputStream inputStream, OutputStream outputStream, ConnectionListener listener, Logger logger) {
        this.frameworkContainer = frameworkContainer;
        this.id = id;
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.listener = listener;
        terminalUserInputBuffer = new ArrayBlockingQueue<JSONObject>(10);
        this.logger = logger;
        start();
    }

    private void start() {
        thread = new Thread(this);
        thread.start();
    }

    public void shutdown() {
        thread.interrupt();
    }

    private void init() {

    }


    @Override
    public void run() {
        init();
        try {
            String data = "";
            String[] messages;
            while (!thread.isInterrupted()) {

                byte[] buffer = new byte[32];
                int recBytes = inputStream.read(buffer);
                data += new String(buffer, ConnectionListener.CHARSET);
                messages = data.split("<EOM>");
                data = messages[messages.length - 1];
                for (int i = 0; i < messages.length - 1; i++) {
                    processMessage(messages[i]);

                }
            }
        } catch (IOException e) {
            listener.connectionDropped(this);
        } finally {
            try {
                JSONObject data = new JSONObject();
                data.put("type", "shutdownNow");
                outputStream.write(data.toJSONString().getBytes(ConnectionListener.CHARSET));
                outputStream.write(ConnectionListener.END_OF_MESSAGE);
                outputStream.flush();
                socket.close();
                inputStream.close();
                outputStream.close();

                if(user != null)
                    logger.printInfo(user.getUsername() +" logged out.");
                else
                    logger.printInfo("Handshake failed.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(JSONObject message) {
        try {
            String type = (String) message.get("type");

            switch (type) {
                case "handshake":
                    makeHandshake(message);
                    break;
                case "command":
                    listener.commandExecutor.execute(() -> {
                        //TODO: Framework.getInstance().getCommandContainer().processInput((String) message.get("command"), user, terminal);
                        sendReadyForNextCommand();
                    });

                    break;
                case "userInput":

                    if (terminalUserInputBuffer.remainingCapacity() == 0) terminalUserInputBuffer.clear();
                    terminalUserInputBuffer.put(message);
                    break;
                case "data":
                    processMessage(unpackData(message));
                    break;
                default:
                    System.out.println(message.toJSONString());
                    break;
            }
        } catch (ParseException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processMessage(String msg) {
        try {
            msg = msg.trim();
            JSONObject message = (JSONObject) (new JSONParser()).parse(msg);
            processMessage(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void sendReadyForNextCommand() {
        try {
            JSONObject data = new JSONObject();
            data.put("type", "readyForNextCmd");
            outputStream.write(data.toJSONString().getBytes(ConnectionListener.CHARSET));
            outputStream.write(ConnectionListener.END_OF_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isLoggedIn() {
        return user != null && token != null;
    }

    public JSONObject unpackData(JSONObject message) throws ParseException {
        String data = cipherHelper.decryptString((String) message.get("data"));
        return (JSONObject) (new JSONParser()).parse(data);
    }

    private void logout() {

    }

    private void makeHandshake(JSONObject message) {
        UserManager manager = frameworkContainer.getUserManager();

        try {
            User user = manager.getUser((String) message.get("username"));
            String pw = user.getPassword();
            token = HashUtils.sha_256(pw + id);
            if (token.equals(message.get("verify"))) {
                this.user = user;
                this.cipherHelper = new CipherHelper(token);
                this.terminal = new RemoteShellTerminal(inputStream, outputStream, cipherHelper, this);
                frameworkContainer.getFrameworkLogger().printInfo(user.getUsername() + " logged in");
                sendReadyForNextCommand();

                return;
            } else {
                thread.interrupt();
                return;
            }
        } catch (UserNotFoundException e) {
            user = null;
            token = null;
            frameworkContainer.getFrameworkLogger().printInfo("Handshake failed");
        }

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return Objects.equals(id, session.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
