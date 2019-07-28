package net.vortexdata.tsManagementBot.modules;

import java.io.File;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;


public class PluginManager {

    private List<PluginInterface> loadedplugins = new ArrayList<PluginInterface>();

    public void enableAll(){
        File[] files = new File("plugins").listFiles();
        for(File f : files)
            loadPlugin(f);
        for(PluginInterface pi : loadedplugins)
            pi.onEnable();
    }
    public void disableAll(){
        for(PluginInterface pi : loadedplugins)
            pi.onDisable();
    }


    public void loadPlugin(File file) {
        try {
            String main = "Bot";
            URL[] urls = { new URL("jar:file:" + file.getPath()+"!/") };
            Class cl = new URLClassLoader(urls).loadClass(main);
            Class[] interfaces = cl.getInterfaces();

            boolean isplugin = false;
            for (int y = 0; y < interfaces.length && !isplugin; y++) {
                System.out.println(interfaces[y].getName());

                if (interfaces[y].getName().equals("net.vortexdata.tsManagementBot.modules.PluginInterface"))
                    isplugin = true;
            }


            if (isplugin) {
                PluginInterface plugin = (PluginInterface) cl.newInstance();
                loadedplugins.add(plugin);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
