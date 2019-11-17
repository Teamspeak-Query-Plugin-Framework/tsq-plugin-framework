package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Interface for Commands
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public abstract class CommandInterface {

    protected ArrayList<UserGroup> groups;
    private Logger logger;


    public CommandInterface(Logger logger) {
        groups = new ArrayList<>();
        this.logger = logger;
    }

    protected Logger getLogger() {
        return logger;
    }

    public int getGroupRange() {
        return groups.size();
    }

    /**
     * This message is shown with the help command.
     *
     * @return Desired help message.
     */
    abstract public String getHelpMessage();

    /**
     * This method is run when the user runs the command.
     */
    abstract public void execute(String[] args, IShell shell);

    /**
     * This method is run when the user runs the command.
     *
     * @return Prefix of the command.
     */
    abstract public String getName();

    public boolean isGroupRequirementMet(UserGroup group) {
        for (UserGroup currentRequiredGroup : groups) {
            if (group == currentRequiredGroup)
                return true;
        }
        return false;
    }

    protected static void allowAllGroups(CommandInterface command) {
        command.groups.addAll(Arrays.asList(UserGroup.values()));
    }
}
