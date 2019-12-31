/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package net.vortexdata.tsqpf.configs;


import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * Parent and wrapper of all framework configs.
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

    /**
     * Loads the config and its values from file to ram.
     * @return true if load was successful.
     */
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

    /**
     * Gets all values from the config.
     *
     * If none have yet been loaded, the default values are returned instead.
     */
    @Override
    public HashMap<String, String> getValues() {
        return values;
    }

    /**
     * @return Returns the config default values.
     */
    @Override
    public HashMap<String, String> getDefaultValues() {
        return defaultValues;
    }

    /**
     * @return Returns the configs path.
     */
    @Override
    public String getPath() {
        return path;
    }

    /**
     *
     * @param key Key of property that should be returned.
     * @return Value associated to key.
     */
    public String getProperty(String key) {
        if (values == null || values.isEmpty())
            return defaultValues.get(key);
        else if (!values.keySet().contains(key))
            return getDefaultProperty(key);
        else
            return values.get(key);
    }

    /**
     *
     * @param key Key of property that should be returned.
     * @return Default value associated to key.
     */
    public String getDefaultProperty(String key) {
        if (defaultValues == null || defaultValues.isEmpty())
            return "";
        else if (!defaultValues.keySet().contains(key))
            return "";
        else
            return defaultValues.get(key);
    }
}
