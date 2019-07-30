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

    private static List<PluginInterface> loadedplugins = Collections.synchronizedList(new ArrayList<PluginInterface>());

    public void enableAll(){
        File[] files = new File("plugins").listFiles();
        if (files == null) return;
        for(File f : files)
            loadPlugin(f);
        for(PluginInterface pi : loadedplugins)
            pi.onEnable(_bot);

    }
    public void disableAll(){
        for(PluginInterface pi : loadedplugins) {
            pi.onDisable();
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
            loadedplugins.add(plugin);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<PluginInterface> getLoadedplugins() {
        return loadedplugins;
    }

}
