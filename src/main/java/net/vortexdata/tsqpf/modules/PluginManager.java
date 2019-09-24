package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;

import java.io.Console;
import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PluginManager {

    private Framework _Framework;

    public PluginManager(Framework Framework) {
        _Framework = Framework;
    }

    private static List<PluginContainer> loadedplugins = Collections.synchronizedList(new ArrayList<PluginContainer>());

    public void enableAll(){
        File pluginDirectory = new File("plugins");
        if (!pluginDirectory.isDirectory() || !pluginDirectory.exists())
            pluginDirectory.mkdirs();

        File[] files = new File("plugins").listFiles();

        if (files == null) return;
        for(File f : files)
            loadPlugin(f);
        for(PluginContainer pc : loadedplugins)
            pc.getPluginInterface().onEnable();

    }
    public void disableAll(){
        for(PluginContainer pc : loadedplugins) {
            pc.getPluginInterface().onDisable();
        }
        loadedplugins.clear();


    }


    public void loadPlugin(File file) {
        try {
            if(file.isDirectory()) return;
            String main = "PluginMain";
            URL[] urls = { new URL("jar:file:" + file.getPath()+"!/") };
            URLClassLoader loader = new URLClassLoader(urls);
            Class cl = loader.loadClass(main);

            if(cl == null) return;


            PluginInterface plugin = (PluginInterface) cl.newInstance();
            if(plugin == null) throw new Error("Error no instance found");
            String name = plugin.getName();
            if(name == null || name.length() < 1) throw new Error("Invalid Name");

            PluginContainer pc = new PluginContainer(plugin, name);
            pc.initLogger(_Framework);
            plugin.setContainer(pc);
            plugin.setBot(_Framework);
            loadedplugins.add(pc);

            _Framework.getLogger().printInfo("Plugin "+pc.getPluginName()+" successfully loaded and initialized.");

        } catch (Exception e) {
            _Framework.getLogger().printInfo("Plugin " + file.getName() + " failed to load. This is probably due to incorrect plugin development setup. Dumping error details: " + e.getMessage());

        }
    }

    public static List<PluginContainer> getLoadedplugins() {
        return loadedplugins;
    }

}
