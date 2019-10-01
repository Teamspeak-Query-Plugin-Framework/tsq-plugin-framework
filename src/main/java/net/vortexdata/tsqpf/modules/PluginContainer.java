package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;

/**
 * Wrapper class combining all plugin essentials like config, logger, api and co.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
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

    /**
     * Initializes the plugins logger class
     *
     * @param _Framework            The Frameworks main class
     */
    public void initLogger(Framework _Framework) {
        if(_logger != null) return;
        _logger = new PluginLogger(_Framework, this);
    }

    /**
     * Returns the plugin interface
     */
    public PluginInterface getPluginInterface() {
        return _pluginInterface;
    }

    /**
     * Returns the plugins name
     */
    public String getPluginName() {
        return _pluginName;
    }

    /**
     * Returns the plugins logger
     */
    public PluginLogger getLogger() {
        return _logger;
    }

    /**
     * Returns the plugins config
     */
    public PluginConfig getPluginConfig() {
        return _pluginConfig;
    }
}
