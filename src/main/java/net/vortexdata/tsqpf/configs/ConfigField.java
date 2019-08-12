package net.vortexdata.tsqpf.configs;


import java.util.HashMap;

public class ConfigField {

    protected String path = "";
    protected HashMap<String, String> values;
    protected HashMap<String, String> defaultValues;

    public ConfigField(String path) {
        this.path = path;
    }

}
