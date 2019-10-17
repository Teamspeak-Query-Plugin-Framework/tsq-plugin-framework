package net.vortexdata.tsqpf.plugins;

import com.amihaiemil.eoyaml.Yaml;
import com.amihaiemil.eoyaml.YamlMapping;
import net.vortexdata.tsqpf.Framework;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Loads, unloads and manages all plugins in the plugin directory.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class PluginManager {

    private static List<PluginContainer> loadedPlugins = Collections.synchronizedList(new ArrayList<PluginContainer>());
    private Framework framework;

    public PluginManager(Framework framework) {
        this.framework = framework;
    }

    /**
     * Returns a list of all loaded plugins
     *
     * @return List of all loaded plugins
     */
    public static List<PluginContainer> getLoadedPlugins() {
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
        for (PluginContainer pc : loadedPlugins)
            pc.getTeamspeakPlugin().onEnable();

    }

    /**
     * Unloads all loaded plugins and disables them, preparing for save shutdown.
     */


    public void disableAll() {
        for (PluginContainer pc : loadedPlugins) {
            pc.getTeamspeakPlugin().onDisable();
            pc = null;
        }
        loadedPlugins.clear();
    }

    /**
     * Loads a specific plugin and tries to enable it.
     */
    public void loadPlugin(File file) {
        try {
            if (file.isDirectory()) return;

            URL[] urls = {new URL("jar:file:" + file.getPath() +"!/")};
            URLClassLoader loader = new URLClassLoader(urls);
            if(loader == null) {
                //Cannot load jar file
                return;
            }
            InputStream stream = loader.getResourceAsStream("plugin.yml");
            if (stream == null) {
                //plugin.yml not found
                return;
            }
            YamlMapping yamlMapping = Yaml.createYamlInput(stream).readYamlMapping();
            String main = yamlMapping.string("main");
            String name = yamlMapping.string("name");
            String version = yamlMapping.string("version");

            if(main == null || name == null || version == null) {
                //Jaml not valid
                return;
            }
            Class cl = loader.loadClass(main);
            if (cl == null) {
                //Main class not found!
                return;
            }
            TeamspeakPlugin plugin = (TeamspeakPlugin) cl.newInstance();
            if (plugin == null) throw new Error("Error no instance found");

            if (name == null || name.length() < 1) throw new Error("Invalid Name");

            PluginContainer pc = new PluginContainer(plugin, name, yamlMapping);
            pc.initLogger(framework);
            plugin.setContainer(pc);
            plugin.setFramework(framework);
            loadedPlugins.add(pc);

            framework.getLogger().printInfo("Plugin " + pc.getPluginName() + " successfully loaded and initialized.");

        } catch (Exception e) {
            framework.getLogger().printInfo("Plugin " + file.getName() + " failed to load. This is probably due to incorrect plugin development setup. Dumping error details: " + e.getMessage());

        }
    }

}
