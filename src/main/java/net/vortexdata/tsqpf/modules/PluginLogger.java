package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.console.Logger;

public class PluginLogger extends Logger {

    private PluginContainer _container;

    public PluginLogger(Framework Framework, PluginContainer container) {
        super(Framework);
        _container = container;
    }

    @Override
    public void printDebug(String message) {
        super.printDebug("["+_container.getPluginName()+"] " +message);
    }

    @Override
    public void printError(String message) {
        super.printError("["+_container.getPluginName()+"] " +message);
    }

    @Override
    public void printInfo(String message) {
        super.printInfo("["+_container.getPluginName()+"] " +message);
    }
}
