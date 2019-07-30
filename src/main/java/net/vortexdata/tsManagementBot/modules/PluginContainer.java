package net.vortexdata.tsManagementBot.modules;

public class PluginContainer {

    private PluginInterface _pluginInterface;
    private String _pluginName;

    public PluginContainer(PluginInterface pluginInterface, String pluginName) {
        _pluginInterface = pluginInterface;
        _pluginName = pluginName;
    }


    public PluginInterface getPluginInterface() {
        return _pluginInterface;
    }

    public String getPluginName() {
        return _pluginName;
    }
}
