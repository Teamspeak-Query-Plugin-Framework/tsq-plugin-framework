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

/**
 * <p>CommandDelUser class.</p>
 *
 * @author Sandro Kierner
 * @since 2.0.0
 * @version $Id: $Id
 */
public class CommandDelUser extends CommandInterface {

    UserManager userManager;

    /**
     * <p>Constructor for CommandDelUser.</p>
     *
     * @param logger a {@link net.vortexdata.tsqpf.console.Logger} object.
     * @param userManager a {@link net.vortexdata.tsqpf.authenticator.UserManager} object.
     */
    public CommandDelUser(Logger logger, UserManager userManager) {
        super(logger);
        this.userManager = userManager;
        groups.add(UserGroup.ROOT);
    }

    /** {@inheritDoc} */
    @Override
    public String getHelpMessage() {
        return "Deletes a user";
    }

    /** {@inheritDoc} */
    @Override
    public void execute(String[] args, IShell shell) {
        if (args.length == 0) {
            shell.getPrinter().println("Please specify a username.");
            return;
        } else {
            if (args[0].equalsIgnoreCase("ROOT")) {
                shell.getPrinter().println("Root user can not be deleted.");
                return;
            }
            boolean success = userManager.deleteUser(args[0]);
            if (success)
                shell.getPrinter().println("User " + args[0] + " deleted.");
            else
                shell.getPrinter().println("User " + args[0] + " does not exist.");
        }
    }

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "deluser";
    }

}
