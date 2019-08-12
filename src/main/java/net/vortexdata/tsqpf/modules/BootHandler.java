package net.vortexdata.tsqpf.modules;

public class BootHandler {

    private long bootStartTime = 0;
    private long bootEndTime = 0;

    public void setBootStartTime() {
        bootStartTime = System.currentTimeMillis();
    }

    public void setBootEndTime() {
        bootEndTime = System.currentTimeMillis();
    }

    public float getBootTime() {
        return bootEndTime - bootStartTime;
    }

}
