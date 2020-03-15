package net.vortexdata.tsqpf.modules.updatefetcher;


import net.vortexdata.tsqpf.framework.FrameworkContainer;
import net.vortexdata.tsqpf.configs.ConfigProject;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * <p>UpdateFetcher class.</p>
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class UpdateFetcher {

    private String updateUrl;
    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for UpdateFetcher.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public UpdateFetcher(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;
        ConfigProject configProject = new ConfigProject(frameworkContainer.getFrameworkLogger());
        configProject.load();
        updateUrl = configProject.getProperty("updateFetcherInterfaceUrl");
    }

    /**
     * <p>checkForUpdate.</p>
     */
    public void checkForUpdate() {
        frameworkContainer.getFrameworkLogger().printDebug("Checking for updates...");
        if (isUpdateAvailable()) {
            System.out.println("================================[UPDATE AVAILABLE]================================");
            System.out.println("There's a newer version of the Teamspeak Query Plugin Framework available for");
            System.out.println("download now!");
            System.out.println("");
            System.out.println("Download the new updatefetcher here:");
            System.out.println("https://github.com/Teamspeak-Query-Plugin-Framework/tsq-plugin-framework/releases");
            System.out.println("==================================================================================");
            System.out.println("Boot process will resume in 8 seconds...");
            try {
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                // Ignore
            }
        } else {
            frameworkContainer.getFrameworkLogger().printInfo("No updates were found.");
        }
    }


    private boolean isUpdateAvailable() {
        ConfigProject cp = new ConfigProject(frameworkContainer.getFrameworkLogger());
        cp.load();
        String curVersion = cp.getProperty("version");
        try {
            JSONParser parser = new JSONParser();
            HttpsURLConnection connection = (HttpsURLConnection) new URL(updateUrl).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            JSONObject data = (JSONObject) parser.parse(response.toString());
            String tag_name = data.get("tag_name").toString();
            return tagToVersionCode(curVersion) < tagToVersionCode(tag_name);

        } catch (MalformedURLException e) {
            frameworkContainer.getFrameworkLogger().printWarn("The update fetcher URL packaged in this framework version seems to be invalid. Maybe you are using a preview build or your framework version is corrupted.");
        } catch (IOException e) {
            frameworkContainer.getFrameworkLogger().printDebug("Failed to fetch updates, skipping...");
        } catch (ParseException e) {
            frameworkContainer.getFrameworkLogger().printWarn("Encountered a parse exception whilst trying to fetch updatefetcher. Maybe you are using a preview build or your framework is corrupted.");
        }
        return false;
    }

    private int tagToVersionCode(String tag) {
        tag = tag.split("-")[0];
        tag = tag.replace(".", "");
        return Integer.parseInt(tag);
    }

}
