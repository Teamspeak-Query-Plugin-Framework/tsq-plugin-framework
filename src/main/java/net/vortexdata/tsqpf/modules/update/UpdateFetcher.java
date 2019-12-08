package net.vortexdata.tsqpf.modules.update;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class UpdateFetcher {

    private static final String updateUrl = "https://api.github.com/repos/VarChar42/simple-brainfuck-interpreter/releases/latest";



    private boolean isUpdateAvailable(String curVersion) {
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
            return tag2VersionCode(curVersion) < tag2VersionCode(tag_name);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int tag2VersionCode(String tag) {
        tag = tag.split("-")[0];
        tag = tag.replace(".", "");
        return Integer.parseInt(tag);
    }

}
