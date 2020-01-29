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

import com.amihaiemil.eoyaml.YamlMapping;
import net.vortexdata.tsqpf.framework.Framework;

/**
 * Wrapper class combining all plugin essentials like config, logger, api and co.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 * @version $Id: $Id
 */
public class PluginContainer {

    private TeamspeakPlugin teamspeakPlugin;
    private String pluginName;
    private PluginLogger pluginLogger;
    private PluginConfig pluginConfig;
    private YamlMapping pluginYamlConfig;

    /**
     * <p>Constructor for PluginContainer.</p>
     *
     * @param teamspeakPlugin a {@link net.vortexdata.tsqpf.plugins.TeamspeakPlugin} object.
     * @param pluginName a {@link java.lang.String} object.
     * @param yamlConfig a {@link com.amihaiemil.eoyaml.YamlMapping} object.
     */
    public PluginContainer(TeamspeakPlugin teamspeakPlugin, String pluginName, YamlMapping yamlConfig) {
        this.teamspeakPlugin = teamspeakPlugin;
        this.pluginName = pluginName;
        this.pluginYamlConfig = yamlConfig;
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
     *
     * @return a {@link net.vortexdata.tsqpf.plugins.TeamspeakPlugin} object.
     */
    public TeamspeakPlugin getTeamspeakPlugin() {
        return teamspeakPlugin;
    }

    /**
     * <p>Getter for the field <code>pluginYamlConfig</code>.</p>
     *
     * @return a {@link com.amihaiemil.eoyaml.YamlMapping} object.
     */
    public YamlMapping getPluginYamlConfig() {
        return this.pluginYamlConfig;
    }
    /**
     * Returns the plugins name
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * Returns the plugins logger
     *
     * @return a {@link net.vortexdata.tsqpf.plugins.PluginLogger} object.
     */
    public PluginLogger getLogger() {
        return pluginLogger;
    }

    /**
     * Returns the plugins config
     *
     * @return a {@link net.vortexdata.tsqpf.plugins.PluginConfig} object.
     */
    public PluginConfig getPluginConfig() {
        return pluginConfig;
    }
}
