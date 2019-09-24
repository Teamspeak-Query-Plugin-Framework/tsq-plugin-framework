package net.vortexdata.tsqpf.configs;


import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

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
                    values.put((String) k, prop.getProperty((String) k));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return values;
    }

}