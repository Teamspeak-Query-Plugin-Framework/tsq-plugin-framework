package net.vortexdata.tsManagementBot.configs;

import java.util.*;

public interface Config {

    public void load();

    public HashMap<String, String> getValues();

    public HashMap<String, String> getDefaultValues();

    public String getPath();

}
