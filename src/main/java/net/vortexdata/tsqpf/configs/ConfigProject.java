package net.vortexdata.tsqpf.configs;

import net.vortexdata.tsqpf.exceptions.OutdatedEulaException;
import org.apache.logging.log4j.core.util.JsonUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class ConfigProject extends Config {

    public ConfigProject() {
        super("/project.properties");
    }

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

        values.forEach((x, b) -> System.out.println(x +"->"+ b));
        return true;
    }

}
