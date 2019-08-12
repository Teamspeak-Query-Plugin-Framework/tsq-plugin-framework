package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;

public class PluginContainer {

    private PluginInterface _pluginInterface;
    private String _pluginName;
    private PluginLogger _logger;
    private PluginConfig _pluginConfig;

    public PluginContainer(PluginInterface pluginInterface, String pluginName) {
        _pluginInterface = pluginInterface;
        _pluginName = pluginName;
        _pluginConfig = new PluginConfig(_pluginName);
    }

    public void initLogger(Framework _Framework) {
        if(_logger != null) return;
        _logger = new PluginLogger(_Framework, this);
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

    public PluginConfig getPluginConfig() {
        return _pluginConfig;
    }
}
