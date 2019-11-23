package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

public class CommandFramework extends CommandInterface {

    private Framework framework;

    public CommandFramework(Logger logger, Framework framework) {
        super(logger);
        this.framework = framework;
        groups.add(UserGroup.ROOT);
    }

    @Override
    public String getHelpMessage() {
        return "Controls framework operations.";
    }

    @Override
    public void execute(String[] args, IShell shell) {


        if (args.length > 0) {


            if (args[0].equalsIgnoreCase("reload")) {
                shell.getPrinter().println("This action is not supported in this build.");
            } else if (args[0].equalsIgnoreCase("stop") || args[0].equalsIgnoreCase("exit") || args[0].equalsIgnoreCase("shutdown")) {
                framework.shutdown();
            } else if (args[0].equalsIgnoreCase("kill")) {
                System.exit(0);
            } else if (args[0].equalsIgnoreCase("stats")) {
                shell.getPrinter().println("Available cores: \t\t\t" + Runtime.getRuntime().availableProcessors());
                shell.getPrinter().println("Memory usage: \t\t\t" + (Runtime.getRuntime().maxMemory() - Runtime.getRuntime().maxMemory()));
                shell.getPrinter().println("Operating system: \t\t\t" + System.getProperty("os.name"));
                shell.getPrinter().println("OS version: \t\t\t\t" + System.getProperty("os.version"));
                shell.getPrinter().println("Java version: \t\t\t" + System.getProperty("java.version"));
                shell.getPrinter().println("Java vendor: \t\t\t" + System.getProperty("java.vendor"));
            } else {
                shell.getPrinter().println("framework "+ args[0] +": unknown parameters");
                shell.getPrinter().println("Try 'framework <reload | kill | stop | stats>'");
            }


        } else {
            shell.getPrinter().println("framework: missing parameters");
            shell.getPrinter().println("Try 'framework <reload | kill | stop | stats>'");
        }


    }

    @Override
    public String getName() {
        return "framework";
    }

}
