package net.vortexdata.tsManagementBot.modules;

import net.vortexdata.tsManagementBot.Bot;
import net.vortexdata.tsManagementBot.console.Logger;

public class PluginContainer {

    private PluginInterface _pluginInterface;
    private String _pluginName;
    private PluginLogger _logger;

    public PluginContainer(PluginInterface pluginInterface, String pluginName) {
        _pluginInterface = pluginInterface;
        _pluginName = pluginName;
    }

    public void initLogger(Bot _bot) {
        if(_logger != null) return;
        _logger = new PluginLogger(_bot, this);
    }


    public PluginInterface getPluginInterface() {
        return _pluginInterface;
    }

    public String getPluginName() {
        return _pluginName;
    }

    public PluginLogger getLogger() {
        return _logger;
    }
}
