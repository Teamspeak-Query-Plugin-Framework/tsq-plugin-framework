package net.vortexdata.tsqpf.modules;


import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.commands.CommandInterface;

public abstract class PluginInterface extends EventHandler {


    private PluginContainer _container = null;
    private Framework _Framework;

    
    protected PluginContainer getContainer() {
        return _container;
    }

    abstract public String getName();


    public void setContainer(PluginContainer pc) {
        if(_container != null) return;
        _container = pc;
    }

    public void setBot(Framework pc) {
        if(_Framework != null) return;
        _Framework = pc;
    }


    protected PluginConfig getConfig() {
        return _container.getPluginConfig();
    }

    protected TS3Api getAPI() {
        return _Framework.getApi();
    }

    protected void  registerCommand(CommandInterface cmd) {
        _Framework.getConsoleHandler().registerCommand(cmd);
    }
    
    protected PluginLogger getLogger() {
        return _container.getLogger();
    }

    abstract public void onEnable();
    abstract public void onDisable();


}
