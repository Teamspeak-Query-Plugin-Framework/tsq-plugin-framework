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
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.exceptions.UserAlreadyExistingException;

/**
 * <p>CommandAddUser class.</p>
 *
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class CommandAddUser extends CommandInterface {

    private UserManager userManager;

    /**
     * <p>Constructor for CommandAddUser.</p>
     *
     * @param logger a {@link net.vortexdata.tsqpf.console.Logger} object.
     * @param userManager a {@link net.vortexdata.tsqpf.authenticator.UserManager} object.
     */
    public CommandAddUser(Logger logger, UserManager userManager) {
        super(logger);
        this.userManager = userManager;
        groups.add(UserGroup.ROOT);
        groups.add(UserGroup.ADMINISTRATOR);
        setDescription("Creates a new user account.");
    }

    /** {@inheritDoc} */
    @Override
    public void execute(String[] args, IShell shell) {

        UserGroup newGroup;
        shell.getPrinter().print("Enter new username: ");
        String username = shell.getReader().nextLine();
        if (username.contains(" ") || username.isEmpty()) {
            shell.getPrinter().println("Username must not contain whitespaces, please try again.");
            return;
        }

        if (!username.matches("[a-zA-Z0-9]+")) {
            shell.getPrinter().println("Username must only contain alphanumeric characters.");
            return;
        }


        shell.getPrinter().print("Enter new password: ");
        String password = shell.getReader().nextLine();
        shell.getPrinter().print("Re-type new password: ");
        String retypePassword = shell.getReader().nextLine();
        if (!password.equals(retypePassword)) {
            shell.getPrinter().println("Passwords don't match, please try again.");
            return;
        }

        shell.getPrinter().print("Group (Administrator / Guest): ");
        String group = shell.getReader().nextLine();
        if (group.equalsIgnoreCase("ADMINISTRATOR"))
            newGroup = UserGroup.ADMINISTRATOR;
        else if (group.equalsIgnoreCase("GUEST"))
            newGroup = UserGroup.GUEST;
        else {
            shell.getPrinter().println(group + " does not match any of the available groups, please try again.");
            return;
        }

        try {
            userManager.createUser(username, password, newGroup);
            shell.getPrinter().println("New user created.");
        } catch (UserAlreadyExistingException e) {
            shell.getPrinter().println("Fatal: User " + username + " already exists.");
            return;
        }

    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "adduser";
    }

}
