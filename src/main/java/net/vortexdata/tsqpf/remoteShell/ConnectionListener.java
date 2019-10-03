package net.vortexdata.tsqpf.remoteShell;

import net.vortexdata.tsqpf.console.Logger;
import org.json.simple.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

public class ConnectionListener implements Runnable {

    private Logger logger;
    private boolean running = false;
    private Thread thread;
    private ArrayList<Session> sessions = new ArrayList<>();
    public static final Charset charset = Charset.forName("UTF-8");


    public ConnectionListener(Logger logger) {
        this.logger = logger;
    }

    public void start() {
        if(running) return;
        if(thread == null ) thread = new Thread(this, this.getClass().getName());
        thread.start();


    }

    public void stop() {
        thread.interrupt();
    }

    public void connectionDropped(Session session){
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


            outputStream.write(handshake.toJSONString().getBytes(charset));
            outputStream.write("<EOM>".getBytes(charset));
            outputStream.flush();
            sessions.add(new Session(id, socket, inputStream, outputStream, this));




        }
        } catch (Exception e) {
            logger.printError(e.getMessage());
        }
    }
}
