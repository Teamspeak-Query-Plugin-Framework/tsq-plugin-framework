package net.vortexdata.tsqpf.console;



import net.vortexdata.tsqpf.remoteShell.CipherHelper;
import net.vortexdata.tsqpf.remoteShell.ConnectionListener;
import net.vortexdata.tsqpf.utils.CipherUtils;
import org.json.simple.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RemoteShellTerminal implements VirtualTerminal {

    private InputStream inputStream;
    private OutputStream outputStream;
    private CipherHelper cipherHelper;


    public RemoteShellTerminal(InputStream inputStream, OutputStream outputStream, CipherHelper helper) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.cipherHelper = helper;
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
            container.put("data",cipherHelper.encryptString(data.toJSONString()));
            outputStream.write(container.toJSONString().getBytes(ConnectionListener.CHARSET));
            outputStream.write(ConnectionListener.END_OF_MESSAGE);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void print(String msg) {
        throw new Error("This method is not available for remote shells.");
    }

    @Override
    public String readln() {
        try {
            JSONObject data = new JSONObject();
            data.put("type", "terminal");
            data.put("mode", "readln");
            outputStream.write(data.toJSONString().getBytes(ConnectionListener.CHARSET));
            outputStream.write(ConnectionListener.END_OF_MESSAGE);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "not implemented yet";
    }
}
