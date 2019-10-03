package net.vortexdata.tsqpf.remoteShell;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Objects;

public class Session implements Runnable {

    private String id;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private Thread thread;
    private ConnectionListener listener;

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
            while (true) {
                byte[] buffer = new byte[32];
                int recBytes = inputStream.read(buffer);
                data += new String(buffer, ConnectionListener.charset);
                messages = data.split("<EOM>");
                data = messages[messages.length-1];
                for (int i = 0; i < messages.length-1; i++) {
                    System.out.println(messages[i]);
                }
            }
        } catch (IOException e) {
            listener.connectionDropped(this);
        }
    }

    private void processMessage(String msg) {
        try {
            JSONObject message = (JSONObject)(new JSONParser()).parse(msg);
            String type = (String) message.get("type");
            System.out.println(type);

        } catch (ParseException e) {
            e.printStackTrace();
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
