package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.exceptions.InvalidConfigPropertyKey;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Plugins config class
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class PluginConfig {

    File configDir;
    private HashMap<String, String> entries;
    public PluginConfig(String name) {
        configDir = new File("plugins//" + name+"//plugin.conf");
        configDir.getParentFile().mkdirs();

        entries = new HashMap<String, String>();
        readAll();
    }

    /**
     * Loads a plugins config values.
     */
    public void readAll() {
        try {
            if (!configDir.exists()) {
                configDir.createNewFile();
            }
            entries.clear();
            BufferedReader reader = new BufferedReader(new FileReader(configDir));
            String line;
            String[] data;
            String key;
            String value;
            while ((line = reader.readLine()) != null)  {
                data = line.split(":");
                if(data.length < 2) continue;
                key = data[0];
                value = String.join(":", Arrays.copyOfRange(data, 1, data.length));
                entries.put(key, value);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Set a plugins config value
     */
    public void setValue(String key, String value) {
        if(entries.containsKey(key)) {
            entries.replace(key, value);
        } else {
            entries.put(key,value);
        }

    }

    /**
     * Set a plugins default values
     *
     * @param key       The values key
     * @param value     The config value
     */
    public void setDefault(String key, String value) {
        if(!containsKey(key)) {
            setValue(key, value);
        }
    }

    /**
     * Tests if a key exists
     *
     * @param key       The key
     * @return          True if key exists
     */
    public boolean containsKey(String key) {
        return entries.containsKey(key);
    }

    public String readValue(String key) {
        return entries.get(key);
    }

    public void clear() {
        entries.clear();
    }


    public void saveAll() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configDir, false));
            for(String key : entries.keySet()) {
                if(key.contains(":")) {
                    writer.close();
                    throw new InvalidConfigPropertyKey("Key must not contain ':'");
                }
                writer.write(key+":"+entries.get(key) + "\n");

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
