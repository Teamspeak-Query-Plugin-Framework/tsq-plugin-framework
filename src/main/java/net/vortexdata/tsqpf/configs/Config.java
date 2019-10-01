package net.vortexdata.tsqpf.configs;


import java.util.HashMap;

/**
 * Configuration parent class prototype
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public interface Config {

    boolean load();

    HashMap<String, String> getValues();

    HashMap<String, String> getDefaultValues();

    String getPath();

}
