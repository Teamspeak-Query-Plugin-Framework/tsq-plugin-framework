package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandClear extends CommandInterface {

    public CommandClear(Logger logger) {
        super(logger);
    }

    @Override
    public String getHelpMessage() {
        return "Clears the console screen";
    }

    @Override
    public void gotCalled(String[] args, VirtualTerminal terminal) {
        for (int i = 0; i < 50; i++)
            terminal.println("");
    }

    @Override
    public String getName() {
        return "clear";
    }

}
