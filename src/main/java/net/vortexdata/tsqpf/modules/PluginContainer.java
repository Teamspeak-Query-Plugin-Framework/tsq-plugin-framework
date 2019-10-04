package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;

/**
 * Wrapper class combining all plugin essentials like config, logger, api and co.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class PluginContainer {

    private PluginInterface pluginInterface;
    private String pluginName;
    private PluginLogger pluginLogger;
    private PluginConfig pluginConfig;

    public PluginContainer(PluginInterface pluginInterface, String pluginName) {
        this.pluginInterface = pluginInterface;
        this.pluginName = pluginName;
        pluginConfig = new PluginConfig(this.pluginName);
    }

    /**
     * Initializes the plugins logger class
     *
     * @param _Framework The Frameworks main class
     */
    public void initLogger(Framework _Framework) {
        if (pluginLogger != null) return;
        pluginLogger = new PluginLogger(_Framework, this);
    }

    /**
     * Returns the plugin interface
     */
    public PluginInterface getPluginInterface() {
        return pluginInterface;
    }



    /**
     * Returns the plugins name
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Returns the plugins logger
     */
    public PluginLogger getLogger() {
        return pluginLogger;
    }

    /**
     * Returns the plugins config
     */
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
}
