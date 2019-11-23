package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Shuts down the framework
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class CommandStop extends CommandInterface {

    private Framework framework;

    public CommandStop(Logger logger, Framework framework) {
        super(logger);
        this.framework = framework;
        groups.add(UserGroup.ROOT);
    }

    @Override
    public String getHelpMessage() {
        return "Unloads all plugins and ends the program.";
    }

    @Override
    public void execute(String[] args, IShell shell) {
        framework.shutdown();
    }

    @Override
    public String getName() {
        return "stop";
    }
}
