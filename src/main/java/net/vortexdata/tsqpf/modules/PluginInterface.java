package net.vortexdata.tsqpf.modules;


import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.listeners.ChatCommandInterface;

/**
 * Parent class for framework plugins PluginMain
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public abstract class PluginInterface extends EventHandler {


    private PluginContainer _container = null;
    private Framework _Framework;

    /**
     * Returns a plugins container
     *
     * @return      Plugin container
     */
    protected PluginContainer getContainer() {
        return _container;
    }

    /**
     * Returns a plugins name
     *
     * @return      Plugin name
     */
    abstract public String getName();

    /**
     * Sets a plugins container
     *
     * @param pc        Plugin Container
     */
    public void setContainer(PluginContainer pc) {
        if(_container != null) return;
        _container = pc;
    }

    /**
     * Sets a plugins framework
     *
     * @param pc        The Framework
     */
    public void setFramework(Framework pc) {
        if(_Framework != null) return;
        _Framework = pc;
    }

    /**
     * Returns the plugins configuration
     *
     * @return      Returns the plugins configuration
     */
    protected PluginConfig getConfig() {
        return _container.getPluginConfig();
    }

    /**
     * Returns the Frameworks api, providing the plugin with all essential teamspeak functions
     *
     * @return      The Frameworks api
     */
    protected TS3Api getAPI() {
        return _Framework.getApi();
    }

    /**
     * Registers a new console handler command
     *
     * @param cmd       The command class
     */
    @Deprecated
    protected void  registerCommand(CommandInterface cmd) {
        _Framework.getConsoleHandler().registerCommand(cmd);
    }

    /**
     * Registers a new console command
     *
     * @param cmd       The command class
     */
    protected void  registerConsoleCommand(CommandInterface cmd) {
        _Framework.getConsoleHandler().registerCommand(cmd);
    }

    /**
     * Registers a new teamspeak direct chat command
     *
     * @param cmd       The command class
     */
    protected void  registerChatCommand(ChatCommandInterface cmd, String txt) {
        _Framework.getChatCommandListener().registerNewCommand(cmd, txt);
    }

    /**
     * Returns a logger for plugin
     *
     * @return      The plugins custom logger
     */
    protected PluginLogger getLogger() {
        return _container.getLogger();
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
