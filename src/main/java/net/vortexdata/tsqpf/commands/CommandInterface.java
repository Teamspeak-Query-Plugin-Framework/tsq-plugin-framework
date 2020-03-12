/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package net.vortexdata.tsqpf.commands;

import javafx.scene.control.*;
import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

import java.util.*;

/**
 * Interface for Commands
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 * @version $Id: $Id
 */
public abstract class CommandInterface {

    protected ArrayList<UserGroup> groups;
    private Logger logger;
    private HashMap<String, String> availableArgs;

    public boolean setAvailableArgs(HashMap<String, String> availableArgs) {
        this.availableArgs = availableArgs;
        return true;
    }

    public boolean addAvailableArg(String name, String description) {
        if (availableArgs == null)
            availableArgs = new HashMap<>();
        availableArgs.put(name, description);
        return true;
    }

    /**
     * <p>Constructor for CommandInterface.</p>
     *
     * @param logger a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    public CommandInterface(Logger logger) {
        groups = new ArrayList<>();
        this.logger = logger;
    }

    /**
     * <p>Getter for the field <code>logger</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    protected Logger getLogger() {
        return logger;
    }

    /**
     * <p>getGroupRange.</p>
     *
     * @return a int.
     */
    public int getGroupRange() {
        return groups.size();
    }

    /**
     * This message is shown with the help command.
     *
     * @return Desired help message.
     */
    public String getHelpMessage() {
        StringBuilder sb = new StringBuilder();

        if (availableArgs == null || availableArgs.size() == 0)
            return "Help message not available.";

        sb.append("Usage: ");
        sb.append(getName());
        sb.append(" <");

        String[] keys = (String[]) availableArgs.keySet().toArray();
        for (int i = 0; i < availableArgs.size(); ++i) {
            if (availableArgs.size() - i == 0) {
                sb.append(keys[i] + ">\n\n");
            } else if (i == 0) {
                sb.append(sb.append(keys[i]));
            } else {
                sb.append(sb.append(" | " + keys[i]) + " | ");
            }
        }

        sb.append("More information about all arguments:\n\n");
        for (String key : availableArgs.keySet()) {
            sb.append("- " + key + ": " + availableArgs.get(key) + "\n");
        }

        return sb.toString();
    };

    /**
     * This method is run when the user runs the command.
     *
     * @param args an array of {@link java.lang.String} objects.
     * @param shell a {@link net.vortexdata.tsqpf.console.IShell} object.
     */
    abstract public void execute(String[] args, IShell shell);

    /**
     * This method is run when the user runs the command.
     *
     * @return Prefix of the command.
     */
    abstract public String getName();

    /**
     * <p>isGroupRequirementMet.</p>
     *
     * @param group a {@link net.vortexdata.tsqpf.authenticator.UserGroup} object.
     * @return a boolean.
     */
    public boolean isGroupRequirementMet(UserGroup group) {
        if (group == null) {
            logger.printError("Failed to check group permission requirement for command " + getName() + ".");
            return false;
        }
        for (UserGroup currentRequiredGroup : groups) {
            if (group == currentRequiredGroup)
                return true;
        }
        return false;
    }

    /**
     * <p>allowAllGroups.</p>
     *
     * @param command a {@link net.vortexdata.tsqpf.commands.CommandInterface} object.
     */
    protected static void allowAllGroups(CommandInterface command) {
        command.groups.addAll(Arrays.asList(UserGroup.values()));
    }
}
