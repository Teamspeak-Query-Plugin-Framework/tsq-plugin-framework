package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.commands.commandFramework.ParameterInterpreter;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandFramework extends CommandInterface {

    private Framework framework;
    private ParameterInterpreter parameterInterpreter;

    public CommandFramework(Logger logger, Framework framework) {
        super(logger);
        this.framework = framework;
    }

    @Override
    public String getHelpMessage() {
        return "Controls framework operations.";
    }

    @Override
    public void gotCalled(String[] args, VirtualTerminal terminal) {


        if (args.length > 1) {


            if (args[1].equalsIgnoreCase("reload")) {
                framework.sleep();
            } else if (args[1].equalsIgnoreCase("stop")) {
                framework.shutdown();
            } else if (args[1].equalsIgnoreCase("kill")) {
                System.exit(0);
            }


        } else {
            terminal.println("framework: missing parameters");
            terminal.println("Try 'framework <reload | kill | stop | stats>'");
        }


    }

    @Override
    public String getName() {
        return "framework";
    }

}
