package net.vortexdata.tsqpf.modules.uncaughtExceptionHandler;

import net.vortexdata.tsqpf.framework.Framework;
import net.vortexdata.tsqpf.framework.FrameworkContainer;

/**
 * <p>ExceptionHandler class.</p>
 *
 * @author Michael Wiesinger
 * @since 2.0.0
 * @version $Id: $Id
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {


    private Framework framework;
    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for ExceptionHandler.</p>
     *
     * @param framework a {@link net.vortexdata.tsqpf.framework.Framework} object.
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public ExceptionHandler(Framework framework, FrameworkContainer frameworkContainer) {
        this.framework = framework;
        this.frameworkContainer = frameworkContainer;
    }



    /** {@inheritDoc} */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        frameworkContainer.getFrameworkLogger().printError("["+t.getName()+"] Unhandled exception: "+ e.getMessage());
        if (frameworkContainer.getConfig("configs//main.properties").getProperty("enableExceptionReporting").equalsIgnoreCase("true")) {
            if(frameworkContainer.getFrameworkStatusReporter() != null) frameworkContainer.getFrameworkStatusReporter().logException(t,e);
        }
    }
}
