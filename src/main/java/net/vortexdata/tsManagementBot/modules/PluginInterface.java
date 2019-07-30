package net.vortexdata.tsManagementBot.modules;


import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsManagementBot.Bot;
import net.vortexdata.tsManagementBot.commands.CommandInterface;
import net.vortexdata.tsManagementBot.console.Logger;

public abstract class PluginInterface extends EventHandler {


    private PluginContainer _container = null;
    private Bot _bot;
    
    protected PluginContainer getContainer() {
        return _container;
    }

    abstract public String getName();


    public void setContainer(PluginContainer pc) {
        if(_container != null) return;
        _container = pc;
    }

    public void setBot(Bot pc) {
        if(_bot != null) return;
        _bot = pc;
    }


    protected TS3Api getAPI() {
        return _bot.getApi();
    }

    protected void  registerCommand(CommandInterface cmd) {
        _bot.getConsoleHandler().registerCommand(cmd);
    }
    
    protected Logger getLogger() {
        return _bot.getLogger();
    }

    abstract public void onEnable();
    abstract public void onDisable();


}
