package net.vortexdata.tsManagementBot.configs;

import java.util.*;

public interface Config {

    void load();

    HashMap<String, String> getValues();

    HashMap<String, String> getDefaultValues();

    String getPath();

}
