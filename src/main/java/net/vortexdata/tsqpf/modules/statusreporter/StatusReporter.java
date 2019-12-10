package net.vortexdata.tsqpf.modules.statusreporter;


import net.vortexdata.tsqpf.FrameworkContainer;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StatusReporter {

    private String uuid;
    private URL url;
    private FrameworkContainer frameworkContainer;

    public StatusReporter(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;
        this.uuid = frameworkContainer.getFrameworkUuidManager().getLoadedUUID();
        try {
            url = new URL("http://localhost/statusApi/");
        } catch (MalformedURLException e) {
            frameworkContainer.getFrameworkLogger().printWarn("Could not get framework UUID, generating new one...");
        }
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

    public void logEvent(StatusEvents event) {

        JSONObject requestData = new JSONObject();
        requestData.put("type", event.toString());
        requestData.put("uuid", uuid);
        requestData.put("date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        sendData(requestData.toString());
    }
}
