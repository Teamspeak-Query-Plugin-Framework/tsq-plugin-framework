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

    public HeartBeatListener(TS3Api api) {
        this.api = api;
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
            listener = new ServerSocket(12343);
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
