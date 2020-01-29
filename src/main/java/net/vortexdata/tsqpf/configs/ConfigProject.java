package net.vortexdata.tsqpf.configs;

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

    /**
     * <p>Constructor for ConfigProject.</p>
     */
    public ConfigProject() {
        super("/project.properties");
    }

    /** {@inheritDoc} */
    @Override
    public boolean load() {

        HashMap<String, String> values = new HashMap<String, String>();

        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("/project.properties"));

            // Load all keys & values
            Set<Object> keys = prop.keySet();
            for (Object k : keys) {
                values.put((String) k, prop.getProperty((String) k));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.values = values;

        return true;
    }

}
