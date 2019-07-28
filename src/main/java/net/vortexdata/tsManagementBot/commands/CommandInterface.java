package net.vortexdata.tsManagementBot.commands;

public interface CommandInterface {

    String name = null;

    void gotCalled(String[] args);
    String getName();



}
