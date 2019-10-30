package net.vortexdata.tsqpf.configs;


import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        load();
    }

    public boolean load() {

        HashMap<String, String> values = new HashMap<String, String>();
        File file = new File(path);
        // net.vortexdata.tsqpf.Test if config exists
        if (!file.exists()) {
            // Return empty map if config is empty.
            values = null;
            this.create();
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

    /**
     * Creates a new config file at the desired path.
     *
     * @return true if creation was successful
     */
    public boolean create() {
        // Purge if file remains
        File file = new File(this.getPath());
        if (file.exists()) {
            file.delete();
        }

        Properties prop = new Properties();
        prop.putAll(this.getDefaultValues());

        File configFile = new File(this.getPath());
        configFile.getParentFile().mkdirs();
        try {
            FileOutputStream fileOut = new FileOutputStream(configFile, false);

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = new Date();

            prop.store(fileOut, "Generated at: " + dateFormat.format(date));
            fileOut.flush();
            fileOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public String getProperty(String key) {
        if (values == null || values.isEmpty())
            return defaultValues.get(key);
        else if (!values.keySet().contains(key))
            getDefaultProperty(key);
        else
            return values.get(key);
    }

    public String getDefaultProperty(String key) {
        if (defaultValues == null || defaultValues.isEmpty())
            return "";
        else if (!defaultValues.keySet().contains(key))
            return "";
        else
            return defaultValues.get(key);
    }

}
