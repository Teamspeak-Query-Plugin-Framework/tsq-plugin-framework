package net.vortexdata.tsqpf.modules;


import com.github.theholywaffle.teamspeak3.TS3Api;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.listeners.ChatCommandInterface;

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

    @Deprecated
    protected void  registerCommand(CommandInterface cmd) {
        _Framework.getConsoleHandler().registerCommand(cmd);
    }

    protected void  registerConsoleCommand(CommandInterface cmd) {
        _Framework.getConsoleHandler().registerCommand(cmd);
    }
    protected void  registerChatCommand(ChatCommandInterface cmd, String txt) {
        _Framework.getChatCommandListener().registerNewCommand(cmd, txt);
    }

    protected PluginLogger getLogger() {
        return _container.getLogger();
    }

    abstract public void onEnable();
    abstract public void onDisable();


}
