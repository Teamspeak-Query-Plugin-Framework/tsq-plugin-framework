package net.vortexdata.tsqpf.remoteShell;


import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.RemoteShellTerminal;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;
import net.vortexdata.tsqpf.utils.CipherUtils;
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
    private RemoteShellTerminal terminal;
    private CipherHelper cipherHelper;


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
                data += new String(buffer, ConnectionListener.CHARSET);
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

    private void processMessage(JSONObject message) {
        try {
            String type = (String) message.get("type");

            switch (type) {
                case "handshake":
                    makeHandshake(message);
                    break;
                case "command":
                    Framework.getInstance().getConsoleHandler().processInput((String)message.get("command"), user, terminal);
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

    private void processMessage(String msg) {
        try {
            msg = msg.trim();
            JSONObject message = (JSONObject)(new JSONParser()).parse(msg);
            processMessage(message);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private boolean isLoggedIn() {
        return user != null && token != null;
    }

    private void unpackData(JSONObject message) throws ParseException {
        String data = cipherHelper.decryptString((String)message.get("data"));
        processMessage((JSONObject)(new JSONParser()).parse(data));
    }



    private void makeHandshake(JSONObject message) {
        UserManager manager = Framework.getInstance().getConsoleHandler().getUserManager();

        try {
            User user = manager.getUser((String) message.get("username"));
            String pw = user.getPassword();
            token = HashUtils.sha_256(pw+id);
            if (token.equals((String)message.get("verify"))) {
                this.user = user;
                this.cipherHelper = new CipherHelper(token);
                this.terminal = new RemoteShellTerminal(inputStream, outputStream, cipherHelper);
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
