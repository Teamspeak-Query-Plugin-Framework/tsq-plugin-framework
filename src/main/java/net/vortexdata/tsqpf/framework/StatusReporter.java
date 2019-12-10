package net.vortexdata.tsqpf.framework;


import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.remoteShell.ConnectionListener;
import org.json.simple.JSONObject;
import sun.net.www.http.HttpClient;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        requestData.put("uuid", "18b36e20-ecee-4396-babf-aa8cec9f134b"); // TODO: Needs to be generated at first startup
        requestData.put("date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        sendData(requestData.toString());



    }



    private void sendData(String data) {
        try {
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            con.setRequestProperty("User-Agent", "TSQPF status reporter");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            try(OutputStream os = con.getOutputStream()) {
                byte[] input = ("data="+data).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String createReporterUUID() {
        return UUID.randomUUID().toString();
    }
}
