package net.vortexdata.tsManagementBot.modules;

import jdk.internal.dynalink.support.BottomGuardingDynamicLinker;
import net.vortexdata.tsManagementBot.Bot;

public abstract class PluginInterface {

    private EventHandler eventHandler;

    abstract public EventHandler getEventHandler();
    abstract public void setEventHandler(EventHandler handler);
    abstract public boolean onEnable(Bot bot);
    abstract public boolean onDisable();


}
