package net.vortexdata.tsManagementBot.modules;

import net.vortexdata.tsManagementBot.Bot;

import java.io.File;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PluginManager {

    private Bot _bot;

    public PluginManager(Bot bot) {
        _bot = bot;
    }

    private static List<PluginContainer> loadedplugins = Collections.synchronizedList(new ArrayList<PluginContainer>());

    public void enableAll(){
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
            String main = "PluginMain";
            URL[] urls = { new URL("jar:file:" + file.getPath()+"!/") };
            URLClassLoader loader = new URLClassLoader(urls);
            Class cl = loader.loadClass(main);

            if(cl == null) return;


            PluginInterface plugin = (PluginInterface) cl.newInstance();
            if(plugin == null) return;
            String name = plugin.getName();
            if(name == null || name.length() < 1) return;

            PluginContainer pc = new PluginContainer(plugin, name);
            plugin.setContainer(pc);
            loadedplugins.add(pc);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<PluginContainer> getLoadedplugins() {
        return loadedplugins;
    }

}
