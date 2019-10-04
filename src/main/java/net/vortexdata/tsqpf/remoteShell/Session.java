package net.vortexdata.tsqpf.remoteShell;


import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;
import net.vortexdata.tsqpf.utils.HashUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.Socket;
import java.util.Base64;
import java.util.Objects;

public class Session implements Runnable {

    private String id;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread thread;
    private ConnectionListener listener;
    private User user = null;
    private String token = null;


    public Session(String id, Socket socket, InputStream inputStream, OutputStream outputStream, ConnectionListener listener) {
        this.id = id;
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.listener = listener;
        start();
    }

    private void start() {
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        try {
            String data = "";
            String[] messages;
            while (!thread.isInterrupted()) {
                byte[] buffer = new byte[32];
                int recBytes = inputStream.read(buffer);
                data += new String(buffer, ConnectionListener.charset);
                messages = data.split("<EOM>");
                data = messages[messages.length-1];
                for (int i = 0; i < messages.length-1; i++) {
                    processMessage(messages[i]);
                }
            }
        } catch (IOException e) {
            listener.connectionDropped(this);
        } finally {
            try {
                socket.close();
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(String msg) {
        try {
            msg = msg.trim();

            JSONObject message = (JSONObject)(new JSONParser()).parse(msg);
            String type = (String) message.get("type");

            switch (type) {
                case "handshake":
                    makeHandshake(message);
                    break;
                case "data":
                    unpackData(message);
                    break;
                default:
                    System.out.println(message.toJSONString());
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private boolean isLoggedIn() {
        return user != null && token != null;
    }

    private void unpackData(JSONObject message) throws ParseException {

        System.out.println(message.get("data"));
        String base64 = (String) message.get("data");
        byte[] dataBytes = Base64.getDecoder().decode(base64);
        String data = new String(Objects.requireNonNull(CipherUtils.decrypt(dataBytes, token.getBytes(ConnectionListener.charset))));
        System.out.println(data);
        JSONObject msg = (JSONObject)(new JSONParser()).parse(data);
        System.out.println(msg.get("command"));
        Framework.getInstance().getConsoleHandler().processInput((String)msg.get("command"), user);
    }

    private void makeHandshake(JSONObject message) {
        UserManager manager = Framework.getInstance().getConsoleHandler().getUserManager();

        try {
            User user = manager.getUser((String) message.get("username"));
            String pw = user.getPassword();
            token = HashUtils.sha_256(pw+id);
            if (token.equals((String)message.get("verify"))) {
                this.user = user;
                Framework.getInstance().getLogger().printInfo(user.getUsername()+ " logged in");

                return;
            } else {
                thread.interrupt();
                return;
            }
        } catch (UserNotFoundException e) {
            user = null;
            token = null;
            Framework.getInstance().getLogger().printInfo("Handshake failed");
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
