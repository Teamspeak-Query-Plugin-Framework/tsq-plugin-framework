package net.vortexdata.tsqpf.configs;


import java.util.HashMap;

public interface Config {

    void load();

    HashMap<String, String> getValues();

    HashMap<String, String> getDefaultValues();

    String getPath();

}
