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
