package net.vortexdata.tsqpf.configs;


import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * Config fields prototype
 *
 * @author Sandro Kierner
 * @since 2.0.0
 */
public class Config implements ConfigInterface {

    protected String path = "";
    protected HashMap<String, String> values;
    protected HashMap<String, String> defaultValues;

    public Config(String path) {
        this.path = path;
    }

    public boolean load() {

        HashMap<String, String> values = new HashMap<String, String>();
        File file = new File(path);
        // net.vortexdata.tsqpf.Test if config exists
        if (!file.exists()) {
            // Return empty map if config is empty.
            values = null;
            return false;
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

        this.values = values;
        return true;

    }

    @Override
    public HashMap<String, String> getValues() {
        return values;
    }

    @Override
    public HashMap<String, String> getDefaultValues() {
        return defaultValues;
    }

    @Override
    public String getPath() {
        return path;
    }

}
