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

package net.vortexdata.tsqpf.plugins;

import net.vortexdata.tsqpf.exceptions.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Plugins config class
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 1.0.0
 * @version $Id: $Id
 */
public class PluginConfig {

    File configDir;
    private HashMap<String, String> entries;

    /**
     * <p>Constructor for PluginConfig.</p>
     *
     * @param name a {@link java.lang.String} object.
     */
    public PluginConfig(String name) {
        configDir = new File("plugins//" + name + "//plugin.conf");
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
            while ((line = reader.readLine()) != null) {
                data = line.split(":");
                if (data.length < 2) continue;
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
     *
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.String} object.
     */
    public void setValue(String key, String value) {
        if (entries.containsKey(key)) {
            entries.replace(key, value);
        } else {
            entries.put(key, value);
        }

    }

    /**
     * Set a plugins default values
     *
     * @param key   The values key
     * @param value The config value
     */
    public void setDefault(String key, String value) {
        if (!containsKey(key)) {
            setValue(key, value);
        }
    }

    /**
     * Tests if a key exists
     *
     * @param key The key
     * @return True if key exists
     */
    public boolean containsKey(String key) {
        return entries.containsKey(key);
    }

    /**
     * <p>readValue.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String readValue(String key) {
        return entries.get(key);
    }

    /**
     * <p>clear.</p>
     */
    public void clear() {
        entries.clear();
    }


    /**
     * <p>saveAll.</p>
     */
    public void saveAll() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(configDir, false));
            for (String key : entries.keySet()) {
                if (key.contains(":")) {
                    writer.close();
                    throw new InvalidConfigPropertyKeyException("Key must not contain ':'");
                }
                writer.write(key + ":" + entries.get(key) + "\n");

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
