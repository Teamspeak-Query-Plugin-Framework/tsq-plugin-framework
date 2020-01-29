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

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import net.vortexdata.tsqpf.configs.*;
import net.vortexdata.tsqpf.framework.*;
import net.vortexdata.tsqpf.modules.statusreporter.*;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;

/**
 * Loads, unloads and manages all plugins in the plugin directory.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 * @version $Id: $Id
 */
public class PluginManager {

    private static HashSet<PluginContainer> loadedPlugins;
    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for PluginManager.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public PluginManager(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;
        loadedPlugins = new HashSet<>();
        File pluginsDir = new File("plugins");
        if (!pluginsDir.isDirectory())
            pluginsDir.mkdirs();
    }

    /**
     * Returns a list of all loaded plugins
     *
     * @return List of all loaded plugins
     */
    public static HashSet<PluginContainer> getLoadedPlugins() {
        return loadedPlugins;
    }

    /**
     * Runs through the plugin directory and tries to load and enable all plugins.
     */
    public void enableAll() {

        File pluginDirectory = new File("plugins");
        if (!pluginDirectory.isDirectory() || !pluginDirectory.exists())
            pluginDirectory.mkdirs();

        File[] files = new File("plugins").listFiles();

        if (files == null) return;
        for (File f : files)
            loadPlugin(f);
        for (PluginContainer pc : loadedPlugins) {
            pc.getTeamspeakPlugin().onEnable();
        }
        frameworkContainer.getFrameworkStatusReporter().logEvent(StatusEvents.PLUGINLOAD);

    }

    /**
     * <p>disablePlugin.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean disablePlugin(String name) {
        PluginContainer removed = null;
        for (PluginContainer pc : loadedPlugins) {
            if (pc.getPluginName().equalsIgnoreCase(name)) {
                pc.getTeamspeakPlugin().onDisable();
                removed = pc;
                break;
            }
        }
        if(removed != null) {
            loadedPlugins.remove(removed);
            return true;
        }
        return false;
    }

    /**
     * Unloads all loaded plugins and disables them, preparing for save shutdown.
     */
    public void disableAll() {
        for (PluginContainer pc : loadedPlugins) {
            pc.getTeamspeakPlugin().onDisable();
            frameworkContainer.getFrameworkLogger().printInfo("Unloading plugin " + pc.getPluginName() + ".");
            pc = null;
        }
        loadedPlugins.clear();
        frameworkContainer.getFrameworkStatusReporter().logEvent(StatusEvents.PLUGINDISABLE);
    }

    /**
     * Loads a specific plugin and tries to enable it.
     *
     * @param file a {@link java.io.File} object.
     */
    public void loadPlugin(File file) {
        try {
            if (file.isDirectory()) return;

            URL[] urls = {new URL("jar:file:" + file.getPath() +"!/")};
            URLClassLoader loader = new URLClassLoader(urls);
            if(loader == null) {
                //Cannot load jar file
                frameworkContainer.getFrameworkLogger().printWarn("Failed to load plugin " + file.getName() + ".");
                return;
            }
            InputStream stream = loader.getResourceAsStream("plugin.yml");
            if (stream == null) {
                //plugin.yml not found
                frameworkContainer.getFrameworkLogger().printWarn("Failed to locate plugin.yml of " + file.getName() + ", therefore cancelled its initialization.");
                return;
            }
            YamlMapping yamlMapping = Yaml.createYamlInput(stream).readYamlMapping();
            String main = yamlMapping.string("main");
            String name = yamlMapping.string("name");
            String version = yamlMapping.string("version");
            String apiVersion = yamlMapping.string("api-version");

            for (PluginContainer loadedPlugin : loadedPlugins) {
                if (loadedPlugin.getPluginName().equalsIgnoreCase(name)) {
                    frameworkContainer.getFrameworkLogger().printWarn("Failed to load a plugin ("+ loadedPlugin.getPluginName() +"): Plugin with same name already in use.");
                    return;
                }
            }


            if(main == null || name == null || version == null) {
                //Yaml not valid
                frameworkContainer.getFrameworkLogger().printWarn("Failed to load a plugin due to incorrect plugin.yml setup.");
                return;
            }
            Class cl = loader.loadClass(main);
            if (cl == null) {
                //Main class not found!
                frameworkContainer.getFrameworkLogger().printWarn("Could not locate main class of plugin " + name + ".");
                return;
            }
            TeamspeakPlugin plugin = (TeamspeakPlugin) cl.newInstance();
            if (plugin == null) {
                frameworkContainer.getFrameworkLogger().printWarn("Plugin instance of " + file.getName() + " could not be found.");
                throw new Error("Error no instance found");
            }

            if (name == null || name.length() < 1) {
                frameworkContainer.getFrameworkLogger().printWarn("Name of plugin " + file.getName() + " is invalid.");
                throw new Error("Invalid Name");
            }

            if (apiVersion == null || apiVersion.isEmpty()) {
                frameworkContainer.getFrameworkLogger().printWarn("Plugin " + name + " does not provide an compatible API version description. If you are running into errors, please check for any compatibility issues.");
            } else {
                ConfigProject cp = new ConfigProject();
                cp.load();
                String[] frameworkversionParts = cp.getProperty("version").split("\\.");
                String[] versionParts = apiVersion.split("\\.");
                if (Integer.parseInt(versionParts[0]) != Integer.parseInt(frameworkversionParts[0])) {
                    frameworkContainer.getFrameworkLogger().printError("Plugin " + name + " is using an unsupported mayor API version ("+apiVersion+") and therefore can not be loaded.");
                    return;
                } else if (Integer.parseInt(versionParts[1]) > Integer.parseInt(frameworkversionParts[1])) {
                    frameworkContainer.getFrameworkLogger().printError("Plugin " + name + " is using an API version ("+apiVersion+") with features not supported by your framework version, therefore it can not be loaded.");
                    return;
                }
            }

            PluginContainer pc = new PluginContainer(plugin, name, yamlMapping);
            pc.initLogger(frameworkContainer.getFramework());
            plugin.setContainer(pc);
            plugin.setFrameworkContainer(frameworkContainer);
            loadedPlugins.add(pc);

            frameworkContainer.getFrameworkLogger().printInfo("Plugin " + pc.getPluginName() + " successfully loaded and initialized.");

        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printWarn("Plugin " + file.getName() + " failed to load. This is probably due to incorrect plugin setup. Dumping error details: " + e.getMessage());
            e.printStackTrace();
        }
    }





}
