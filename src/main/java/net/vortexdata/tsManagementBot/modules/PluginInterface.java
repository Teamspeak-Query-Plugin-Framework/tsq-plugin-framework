package net.vortexdata.tsManagementBot.modules;


import net.vortexdata.tsManagementBot.Bot;

public abstract class PluginInterface extends EventHandler {


    private PluginContainer container = null;



    protected PluginContainer getContainer() {
        return container;
    }

    abstract public String getName();


    public void setContainer(PluginContainer pc) {
        if(container != null) return;
        container = pc;
    }


    protected Bot getBot() {
        return Bot.getBot();
    }

    protected void printToConsole(String s) {
        Bot.getBot().getLogger().info("["+getName()+"] "+s);
    }

    abstract public void onEnable();
    abstract public void onDisable();


}
