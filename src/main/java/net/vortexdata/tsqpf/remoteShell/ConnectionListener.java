package net.vortexdata.tsqpf.remoteShell;

import net.vortexdata.tsqpf.console.Logger;
import org.json.simple.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;

public class ConnectionListener implements Runnable {

    public static final Charset CHARSET = StandardCharsets.UTF_8;
    public static final byte[] END_OF_MESSAGE = "<EOM>".getBytes(CHARSET);
    private Logger logger;
    private boolean running = false;
    private Thread thread;
    private ArrayList<Session> sessions = new ArrayList<>();
    private int port;


    public ConnectionListener(Logger logger, int port) {
        this.logger = logger;
        this.port = port;
    }

    public void start() {
        if (running) return;
        if (thread == null) thread = new Thread(this, this.getClass().getName());
        thread.start();


    }

    public void stop() {
        thread.interrupt();
    }

    public void connectionDropped(Session session) {
        sessions.remove(session);
    }

    @Override
    public void run() {
        try {


            ServerSocket listener = new ServerSocket(12342);
            running = true;
            while (running) {
                if (thread.isInterrupted()) {
                    listener.close();
                    running = false;
                    break;
                }
                Socket socket = listener.accept();

                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                JSONObject handshake = new JSONObject();
                SecureRandom random = new SecureRandom();
                byte[] bytes = new byte[32];
                random.nextBytes(bytes);
                String id = Base64.getEncoder().encodeToString(bytes);
                handshake.put("type", "handshake");
                handshake.put("sessionId", id);


                outputStream.write(handshake.toJSONString().getBytes(CHARSET));
                outputStream.write(END_OF_MESSAGE);
                outputStream.flush();
                sessions.add(new Session(id, socket, inputStream, outputStream, this));


            }
        } catch (Exception e) {
            logger.printError(e.getMessage());
        }
    }
}
