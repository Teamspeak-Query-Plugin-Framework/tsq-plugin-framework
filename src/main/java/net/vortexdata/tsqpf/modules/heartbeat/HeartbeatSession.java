package net.vortexdata.tsqpf.modules.heartbeat;

import net.vortexdata.tsqpf.framework.FrameworkContainer;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HeartbeatSession extends Thread {

    private Socket socket;
    private FrameworkContainer container;

    public HeartbeatSession(Socket socket, FrameworkContainer container) {
        this.socket = socket;
        this.container = container;
    }

    @Override
    public void run() {
        try {
            container.getFrameworkLogger().printDebug("Sending heartbeat to " + socket.getRemoteSocketAddress() + ".");
            JSONObject response = new JSONObject();
            response.put("type", "heartbeat");
            response.put("frameworkStatus", container.getFrameworkStatus().toString());
            socket.getOutputStream().write(response.toJSONString().getBytes(StandardCharsets.UTF_8));
            socket.getOutputStream().flush();
        } catch (IOException e) {
            container.getFrameworkLogger().printError("Failed to send heartbeat, appending error information: " + e.getMessage());
        } finally {
            try {
                // Sleep in order to get receiver to close connection properly
                Thread.sleep(100);
                socket.getOutputStream().close();
            } catch (IOException e) {
                container.getFrameworkLogger().printError("Failed to close heartbeat socket, appending error information: " +e.getMessage());
            } catch (InterruptedException e) {
                container.getFrameworkLogger().printError("Heartbeat session close delay interrupted was interrupted, appending error information: " + e.getMessage());
            }
        }
    }

}
