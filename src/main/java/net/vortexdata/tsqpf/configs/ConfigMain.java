package net.vortexdata.tsqpf.configs;

import java.util.HashMap;

/**
 * ConfigMain class
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class ConfigMain extends Config {

    public ConfigMain() {
        super("configs//main.properties");
        // Creating HashMaps
        defaultValues = new HashMap<String, String>();
        values = new HashMap<String, String>();
        // Setting Default Values
        defaultValues.put("serverAddress", "127.0.0.1");
        defaultValues.put("queryPort", "10011");
        defaultValues.put("queryUser", "admin");
        defaultValues.put("queryPassword", "password");
        defaultValues.put("virtualServerId", "1");
        defaultValues.put("clientNickname", "TSQP Framework");
        defaultValues.put("reconnectStrategy", "exponentialBackoff");
        defaultValues.put("messageChatCommandNotFound", "Unknown command.");
        defaultValues.put("shellPort", "12342");
    }

}