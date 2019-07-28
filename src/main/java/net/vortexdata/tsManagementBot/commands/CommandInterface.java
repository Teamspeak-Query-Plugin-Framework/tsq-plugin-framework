package net.vortexdata.tsManagementBot.commands;

public interface CommandInterface {


    String getHelpMessage();
    void gotCalled(String[] args);
    String getName();



}
