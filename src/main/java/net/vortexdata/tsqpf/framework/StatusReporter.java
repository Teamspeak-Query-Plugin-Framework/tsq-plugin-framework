package net.vortexdata.tsqpf.framework;


import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.remoteShell.ConnectionListener;
import org.json.simple.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class StatusReporter {

    private Framework framework;
    private String uuid;
    URL url;

    public StatusReporter(Framework framework, String uuid) {
        this.framework = framework;
        this.uuid = uuid;
        try {
            url = new URL("http://localhost/statusApi/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void startup() {
        JSONObject requestData = new JSONObject();
        requestData.put("type", "startUp");
        requestData.put("uuid", uuid);
        sendData(requestData.toJSONString());



    }



    private void sendData(String data) {
        try {
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = data.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                System.out.println(response.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createReporterUUID() {
        return UUID.randomUUID().toString();
    }
}
