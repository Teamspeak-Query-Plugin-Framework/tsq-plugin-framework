package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.Logger;

public class CommandClear extends CommandInterface {

    public CommandClear(Logger logger) {
        super(logger);
    }

    @Override
    public String getHelpMessage() {
        return "Clears the console screen";
    }

    @Override
    public void gotCalled(String[] args) {
        for (int i = 0; i < 50; i++)
            System.out.println("");
    }

    @Override
    public String getName() {
        return "clear";
    }

}
