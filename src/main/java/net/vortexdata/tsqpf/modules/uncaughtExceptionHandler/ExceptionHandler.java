package net.vortexdata.tsqpf.modules.uncaughtExceptionHandler;

import net.vortexdata.tsqpf.framework.Framework;
import net.vortexdata.tsqpf.framework.FrameworkContainer;

/**
 * @author Michael Wiesinger
 * @since 2.0.0
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


    private Framework framework;
    private FrameworkContainer frameworkContainer;

    public ExceptionHandler(Framework framework, FrameworkContainer frameworkContainer) {
        this.framework = framework;
        this.frameworkContainer = frameworkContainer;
    }



    @Override
    public void uncaughtException(Thread t, Throwable e) {
        frameworkContainer.getFrameworkLogger().printError("["+t.getName()+"] Unhandled exception: "+ e.getMessage());
        if (frameworkContainer.getConfig("configs//main.properties").getProperty("enableExceptionReporting").equalsIgnoreCase("true")) {
            if(frameworkContainer.getFrameworkStatusReporter() != null) frameworkContainer.getFrameworkStatusReporter().logException(t,e);
        }
    }
}
