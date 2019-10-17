package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.remoteShell.CipherHelper;
import net.vortexdata.tsqpf.remoteShell.ConnectionListener;
import net.vortexdata.tsqpf.remoteShell.Session;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class RemoteShellTerminal implements VirtualTerminal {

    private InputStream inputStream;
    private OutputStream outputStream;
    private CipherHelper cipherHelper;
    private Session session;


    public RemoteShellTerminal(InputStream inputStream, OutputStream outputStream, CipherHelper helper, Session session) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.cipherHelper = helper;
        this.session = session;
    }


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

    @Override
    public void print(String msg) {

        println(msg);
    }

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
                if (session.terminalUserInputBuffer.isEmpty()) {
                    Thread.sleep(10);
                    continue;
                }
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
}
