package net.vortexdata.tsqpf.configs;


import java.util.HashMap;

/**
 * Config fields prototype
 *
 * @author Sandro Kierner
 * @since 2.0.0
 */
public class ConfigField {

    protected String path = "";
    protected HashMap<String, String> values;
    protected HashMap<String, String> defaultValues;

    public ConfigField(String path) {
        this.path = path;
    }

}
