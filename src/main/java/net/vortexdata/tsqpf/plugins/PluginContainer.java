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
import net.vortexdata.tsqpf.Framework;

/**
 * Wrapper class combining all plugin essentials like config, logger, api and co.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class PluginContainer {

    private TeamspeakPlugin teamspeakPlugin;
    private String pluginName;
    private PluginLogger pluginLogger;
    private PluginConfig pluginConfig;
    private YamlMapping pluginYamlConfig;

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
     */
    public TeamspeakPlugin getTeamspeakPlugin() {
        return teamspeakPlugin;
    }

    public YamlMapping getPluginYamlConfig() {
        return this.pluginYamlConfig;
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
