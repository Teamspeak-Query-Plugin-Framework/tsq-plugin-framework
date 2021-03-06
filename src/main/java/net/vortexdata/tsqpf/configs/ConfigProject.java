package net.vortexdata.tsqpf.configs;

import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.exceptions.OutdatedEulaException;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * <p>ConfigProject class.</p>
 *
 * @author TAXSET
 * @since 2.0.0
 *
 * This config is a derivative config for configs contained in the frameworks resource directory.
 * @version $Id: $Id
 */
public class ConfigProject extends Config {

    HashMap<String, String> values;

    /**
     * <p>Constructor for ConfigProject.</p>
     */
    public ConfigProject(Logger logger) {
        super("/project.properties", logger);
    }

    /** {@inheritDoc} */
    @Override
    public boolean load() {

        values = new HashMap<>();

        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("/project.properties"));

            // Load all keys & values
            Set<Object> keys = prop.keySet();
            for (Object k : keys) {
                values.put(k.toString(), prop.getProperty(k.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public String getProperty(String key) {
        return values.get(key);
    }

}
