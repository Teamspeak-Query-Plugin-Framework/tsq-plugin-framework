package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandClear extends CommandInterface {

    public CommandClear(Logger logger, VirtualTerminal terminal) {
        super(logger, terminal);
    }

    @Override
    public String getHelpMessage() {
        return "Clears the console screen";
    }

    @Override
    public void gotCalled(String[] args) {
        for (int i = 0; i < 50; i++)
            terminal.println("");
    }

    @Override
    public String getName() {
        return "clear";
    }

}
