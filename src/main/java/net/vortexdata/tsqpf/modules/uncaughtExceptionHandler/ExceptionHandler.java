package net.vortexdata.tsqpf.modules.uncaughtExceptionHandler;

import net.vortexdata.tsqpf.framework.Framework;
import net.vortexdata.tsqpf.framework.FrameworkContainer;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


    private Framework framework;
    private FrameworkContainer frameworkContainer;

    public ExceptionHandler(Framework framework, FrameworkContainer frameworkContainer) {
        this.framework = framework;
        this.frameworkContainer = frameworkContainer;
    }



    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("["+t.getName()+"] Exception: "+ e.getMessage());
        if(frameworkContainer.getFrameworkStatusReporter() != null) frameworkContainer.getFrameworkStatusReporter().logException(t,e);
    }
}
