package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

public class CommandClear extends CommandInterface {

    public CommandClear(Logger logger) {
        super(logger);
        CommandInterface.allowAllGroups(this);
    }

    @Override
    public String getHelpMessage() {
        return "Clears the console screen";
    }

    private static final String clearString = new String(new char[50]).replace('\0','\n');

    @Override
    public void execute(String[] args, IShell shell) {
        shell.getPrinter().print(clearString);
    }

    @Override
    public String getName() {
        return "clear";
    }

}
