package net.vortexdata.tsManagementBot.modules;

import jdk.internal.dynalink.support.BottomGuardingDynamicLinker;
import net.vortexdata.tsManagementBot.Bot;

public abstract class PluginInterface {


    abstract boolean onEnable();
    abstract boolean onDisable();
    abstract Bot getBot();

}
