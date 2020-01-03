package net.vortexdata.tsqpf.modules.statusreporter;


import net.vortexdata.tsqpf.framework.FrameworkContainer;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Core of the status reports that are sent to VortexdataNET for debugging reasons.
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class StatusReporter {

    private String uuid;
    private URL url;
    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for StatusReporter.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public StatusReporter(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;
        this.uuid = frameworkContainer.getFrameworkUuidManager().getLoadedUUID();
        try {
            url = new URL(frameworkContainer.getConfig("/project.properties").getProperty("statisticsInterfaceUrl"));
        } catch (MalformedURLException e) {
            frameworkContainer.getFrameworkLogger().printWarn("Status reporter submission URL is invalid.");
        }
    }

    private void sendData(String data) {
        try {
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");

            con.setRequestProperty("User-Agent", "TSQPF status reporter");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


            try {
                OutputStream os = con.getOutputStream();
                byte[] input = ("data="+data).getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            } catch (Exception e) {
                frameworkContainer.getFrameworkLogger().printError("Encountered an error whilst trying to send data, appending details: " + e.getMessage());
            }

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

            }

        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to send data, appending details: " + e.getMessage());
        }
    }

    /**
     * <p>logEvent.</p>
     *
     * @param event a {@link net.vortexdata.tsqpf.modules.statusreporter.StatusEvents} object.
     */
    public void logEvent(StatusEvents event) {

        JSONObject requestData = new JSONObject();
        requestData.put("type", "event");
        requestData.put("event", event.toString());
        requestData.put("uuid", uuid);
        requestData.put("date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));

        sendData(requestData.toString());
    }

    /**
     * <p>logException.</p>
     *
     * @param t a {@link java.lang.Thread} object.
     * @param e a {@link java.lang.Throwable} object.
     */
    public void logException(Thread t, Throwable e) {

        int supportPin = (int) (Math.random() * Integer.MAX_VALUE);


        frameworkContainer.getFrameworkLogger().printInfo("An unhandled exception has been sent to VortexdataNET servers for further analysis (Report-ID: "+ supportPin +" | Framework UUID: "+ uuid +").");

        JSONObject requestData = new JSONObject();
        requestData.put("type", "exception");
        requestData.put("name", e.getClass().getName());
        requestData.put("uuid", uuid);
        requestData.put("supportPin", supportPin);
        requestData.put("thread", t.getName());
        requestData.put("exceptionMessage", e.toString());
        requestData.put("exceptionStack", e.fillInStackTrace().toString());
        requestData.put("date", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        sendData(requestData.toString());

    }
}
