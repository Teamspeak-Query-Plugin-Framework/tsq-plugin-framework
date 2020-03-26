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


import net.vortexdata.tsqpf.console.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Parent and wrapper of all framework configs.
 *
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class Config implements ConfigInterface {

    private boolean isRegenerated = false;
    protected String path = "";
    protected ArrayList<ConfigValue> values;
    protected ArrayList<ConfigValue> defaultValues;
    protected Logger logger;

    /**
     * <p>Constructor for Config.</p>
     *
     * @param path a {@link java.lang.String} object.
     */
    public Config(String path, Logger logger) {
        this.path = path;
        defaultValues = new ArrayList<>();
        values = new ArrayList<>();
        this.logger = logger;
    }

    /**
     * Loads the config and its values from file to ram.
     *
     * @return true if load was successful.
     */
    public boolean load() {

        HashMap<String, String> values = new HashMap<>();
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

        this.values = ConfigUtils.getArrayFromHashmap(defaultValues, values);
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
        prop.putAll(ConfigUtils.getHashmapFromArray(defaultValues));

        File configFile = new File(this.getPath());
        configFile.getParentFile().mkdirs();
        try {
            FileOutputStream fileOut = new FileOutputStream(configFile, false);

            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = new Date();

            prop.store(fileOut, "Generated on: " + dateFormat.format(date));
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
     * {@inheritDoc}
     *
     * Gets all values from the config.
     *
     * If none have yet been loaded, the default values are returned instead.
     */
    @Override
    public ArrayList<ConfigValue> getValues() {
        return values;
    }

    /** {@inheritDoc} */
    @Override
    public ArrayList<ConfigValue> getDefaultValues() {
        return defaultValues;
    }

    /** {@inheritDoc} */
    @Override
    public String getPath() {
        return path;
    }

    /**
     * <p>getProperty.</p>
     *
     * @param key Key of property that should be returned.
     * @return Value associated to key.
     */
    public String getProperty(String key) {
        if (values == null)
            return "";
        for (ConfigValue value : values) {
            if (value.getKey().equalsIgnoreCase(key))
                return value.getValue();
        }
        return "";
    }

    /**
     * <p>getDefaultProperty.</p>
     *
     * @param key Key of property that should be returned.
     * @return Default value associated to key.
     */
    public String getDefaultProperty(String key) {
        if (defaultValues == null)
            return "";
        for (ConfigValue value : defaultValues) {
            if (value.getKey().equalsIgnoreCase(key))
                return value.getValue();
        }
        return "";

    }

    /**
     * Adds a new default value to defaultValues ConfigValue array.
     * @param key       Key of value
     * @param value     Value of key
     * @param type      CheckType which the value will be checked by.
     * @return          true if added successfully.
     */
    public boolean setDefaultValue(String key, String value, CheckType type) {
        defaultValues.add(
                new ConfigValue(key, value, type)
        );
        return true;
    }

    /**
     * Checks config for incorrect configuration
     * @return false if config is configured incorrectly.
     */
    public boolean runCheck() {
        logger.printDebug("Running check for config " + path + ".");

        if (defaultValues.size() > values.size()) {
            logger.printWarn("Config " + path + " seems to be missing keys, trying to regenerate...");
            regenerate();
        }

        boolean isValid = true;
        for (ConfigValue value : values) {
            if (!value.check()) {
                logger.printError("Config check for value in config " + path + " failed. Key " + value.getKey() + " is not a(n) " + value.getType().toString() + ".");
                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Used to add missing config keys to existing config.
     * @return  true if regeneration was successful, false if failed.
     */
    public boolean regenerate() {

        isRegenerated = true;
        ArrayList<ConfigValue> valuesToWrite = new ArrayList();

        for (ConfigValue defaultValue : defaultValues) {
            ConfigValue loadedConfigValue = getConfigValueByKey(defaultValue.getKey());
            if (loadedConfigValue == null)
                valuesToWrite.add(defaultValue);
            else
                valuesToWrite.add(loadedConfigValue);
        }

        Properties prop = new Properties();
        prop.putAll(ConfigUtils.getHashmapFromArray(valuesToWrite));
        try {
            FileOutputStream fileOut = new FileOutputStream(new File(path), false);
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            Date date = new Date();
            prop.store(fileOut, "Regenerated on: " + dateFormat.format(date));
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
     * Returns ConfigValue by key.
     * @param key   Key of the ConfigValue
     * @return  ConfigValue by key.
     */
    public ConfigValue getConfigValueByKey(String key) {
        for (ConfigValue cv : values) {
            if (cv.getKey().equalsIgnoreCase(key))
                return cv;
        }
        return null;
    }

    /**
     * Returns if the config has been regenerated (eg. due to missing keys).
     * @return  true if config has been regenerated.
     */
    public boolean isRegenerated() {
        return isRegenerated;
    }

}
