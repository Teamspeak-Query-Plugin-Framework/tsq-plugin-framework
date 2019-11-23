package net.vortexdata.tsqpf.modules.boothandler;

/**
 * Records statistics during boot process
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class BootHandler {

    private long bootStartTime = 0;
    private long bootEndTime = 0;

    /**
     * Sets the timestamp of when the boot process started.
     */
    public void setBootStartTime() {
        bootStartTime = System.currentTimeMillis();
    }

    /**
     * Sets the timestamp of when the boot process finished.
     */
    public void setBootEndTime() {
        bootEndTime = System.currentTimeMillis();
    }

    /**
     * @return Returns the total time that passed during boot
     */
    public float getBootTime() {
        return bootEndTime - bootStartTime;
    }

}
