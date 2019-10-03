package net.vortexdata.tsqpf.remoteShell;

import net.vortexdata.tsqpf.console.Logger;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class ConnectionListener implements Runnable {

    private Logger logger;
    private boolean running = false;
    private Thread thread;


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
            outputStream.write("Hallo".getBytes(Charset.forName("UTF-8")));
            socket.close();

        }
        } catch (Exception e) {
            logger.printError(e.getMessage());
        }
    }
}
