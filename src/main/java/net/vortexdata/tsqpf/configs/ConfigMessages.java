package net.vortexdata.tsqpf.configs;

import java.util.HashMap;

public class ConfigMessages extends Config {

    public ConfigMessages() {
        super("configs//messages.properties");
        // Creating HashMaps
        defaultValues = new HashMap<String, String>();
        values = new HashMap<String, String>();
        // Setting Default Values
        defaultValues.put("chatCommandUnknown", "Command not found.");
        defaultValues.put("shellMotd", "You are connected to the TSQPF remote shell. UNAUTHORIZED ACCESS IS PROHIBITED!");
    }

}
