package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandFramework extends CommandInterface {

    private Framework framework;

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
                terminal.println("This action is not supported in this build.");
            } else if (args[1].equalsIgnoreCase("stop") || args[1].equalsIgnoreCase("exit") || args[1].equalsIgnoreCase("shutdown")) {
                framework.shutdown();
            } else if (args[1].equalsIgnoreCase("kill")) {
                System.exit(0);
            } else if (args[1].equalsIgnoreCase("stats")) {
                terminal.println("Available cores: \t\t\t" + Runtime.getRuntime().availableProcessors());
                terminal.println("Memory usage: \t\t\t" + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().maxMemory()));
                terminal.println("Operating system: \t\t\t" + System.getProperty("os.name"));
                terminal.println("OS version: \t\t\t\t" + System.getProperty("os.version"));
                terminal.println("Java version: \t\t\t" + System.getProperty("java.version"));
                terminal.println("Java vendor: \t\t\t" + System.getProperty("java.vendor"));
            } else {
                terminal.println("framework "+ args[1] +": unknown parameters");
                terminal.println("Try 'framework <reload | kill | stop | stats>'");
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
