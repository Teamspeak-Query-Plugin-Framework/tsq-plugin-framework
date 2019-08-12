package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;

import java.io.File;

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
            if(plugin == null) throw new Exception("Error no instance found");
            String name = plugin.getName();
            if(name == null || name.length() < 1) throw new Exception("Invalid Name");

            PluginContainer pc = new PluginContainer(plugin, name);
            pc.initLogger(_Framework);
            plugin.setContainer(pc);
            plugin.setBot(_Framework);
            loadedplugins.add(pc);

            _Framework.getLogger().printInfo("Successfully loaded "+pc.getPluginName());



        } catch (Exception e) {
            _Framework.getLogger().printInfo(file.getName() + " not loaded!!");
            _Framework.getLogger().printDebug(file.getPath() + "not loaded!! "+e.getMessage());
        }
    }

    public static List<PluginContainer> getLoadedplugins() {
        return loadedplugins;
    }

}
