/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package net.vortexdata.tsqpf.plugins;


import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsqpf.*;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.console.CommandContainer;
import net.vortexdata.tsqpf.listeners.ChatCommandInterface;
import net.vortexdata.tsqpf.modules.eventhandler.EventHandler;

/**
 * Parent class for framework plugins PluginMain
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public abstract class TeamspeakPlugin extends EventHandler {


    private PluginContainer pluginContainer = null;
    private FrameworkContainer frameworkContainer;

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
     * @param frameworkContainer The FrameworkContainer
     */
    public void setFrameworkContainer(FrameworkContainer frameworkContainer) {
        if (this.frameworkContainer == null)
            this.frameworkContainer = frameworkContainer;
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
        return frameworkContainer.getTs3Api();
    }

    /**
     * Registers a new console handler command
     *
     * @param cmd The command class
     */
    @Deprecated
    protected void registerCommand(CommandInterface cmd) {
        CommandContainer.registerCommand(cmd);
    }

    /**
     * Registers a new console command
     *
     * @param cmd The command class
     */
    protected void registerConsoleCommand(CommandInterface cmd) {
        CommandContainer.registerCommand(cmd);
    }

    /**
     * Registers a new teamspeak direct chat command
     *
     * @param cmd The command class
     */
    protected void registerChatCommand(ChatCommandInterface cmd, String prefix) {
        frameworkContainer.getFrameworkChatCommandListener().registerNewCommand(cmd, prefix);
    }

    /**
     * Returns a logger for plugin
     *
     * @return The plugins custom logger
     */
    protected PluginLogger getLogger() {
        return pluginContainer.getLogger();
    }


    protected PluginManager getPluginManager() {
        return frameworkContainer.getFrameworkPluginManager();
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
