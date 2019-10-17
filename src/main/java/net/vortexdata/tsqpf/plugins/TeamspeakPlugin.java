package net.vortexdata.tsqpf.plugins;


import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.listeners.ChatCommandInterface;
import net.vortexdata.tsqpf.modules.EventHandler;

/**
 * Parent class for framework plugins PluginMain
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public abstract class TeamspeakPlugin extends EventHandler {


    private PluginContainer pluginContainer = null;
    private Framework framework;

    /**
     * Returns a plugins container
     *
     * @return Plugin container
     */
    protected PluginContainer getContainer() {
        return pluginContainer;
    }

    /**
     * Sets a plugins container
     *
     * @param pc Plugin Container
     */
    public void setContainer(PluginContainer pc) {
        if (pluginContainer != null) return;
        pluginContainer = pc;
    }


    /**
     * Sets a plugins framework
     *
     * @param pc The Framework
     */
    public void setFramework(Framework pc) {
        if (framework != null) return;
        framework = pc;
    }

    /**
     * Returns the plugins configuration
     *
     * @return Returns the plugins configuration
     */
    protected PluginConfig getConfig() {
        return pluginContainer.getPluginConfig();
    }

    /**
     * Returns the Frameworks api, providing the plugin with all essential teamspeak functions
     *
     * @return The Frameworks api
     */
    protected TS3Api getAPI() {
        return framework.getApi();
    }

    /**
     * Registers a new console handler command
     *
     * @param cmd The command class
     */
    @Deprecated
    protected void registerCommand(CommandInterface cmd) {
        framework.getConsoleHandler().registerCommand(cmd);
    }

    /**
     * Registers a new console command
     *
     * @param cmd The command class
     */
    protected void registerConsoleCommand(CommandInterface cmd) {
        framework.getConsoleHandler().registerCommand(cmd);
    }

    /**
     * Registers a new teamspeak direct chat command
     *
     * @param cmd The command class
     */
    protected void registerChatCommand(ChatCommandInterface cmd, String prefix) {
        framework.getChatCommandListener().registerNewCommand(cmd, prefix);
    }

    /**
     * Returns a logger for plugin
     *
     * @return The plugins custom logger
     */
    protected PluginLogger getLogger() {
        return pluginContainer.getLogger();
    }

    /**
     * This is run on plugins enable, initiated by PluginManager
     */
    abstract public void onEnable();

    /**
     * Disables the plugins, usually run by PluginManger on reloads or Framework shutdown.
     */
    abstract public void onDisable();


}
