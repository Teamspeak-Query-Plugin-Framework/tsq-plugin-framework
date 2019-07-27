package net.vortexdata.tsManagementBot.configs;

import net.vortexdata.tsManagementBot.installers.*;

import java.io.*;
import java.util.*;

public class ConfigLoader {

    public HashMap<String, String> load(String path) {

        HashMap<String, String> values = new HashMap<String, String>();
        File file = new File(path);
        // Test if config exists
        if (!file.exists()) {
            // Return empty map if config is empty.
            return values;
        } else {
            try {
                Properties prop = new Properties();
                prop.load(new FileInputStream(path));

                // Load all keys & values
                Set<Object> keys = prop.keySet();
                for (Object k : keys) {
                    values.put((String)k, prop.getProperty((String)k));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return values;
    }

}
