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

import net.vortexdata.tsqpf.console.CommandContainer;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Displays help on console
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 * @version $Id: $Id
 */
public class CommandHelp extends CommandInterface {

    /**
     * <p>Constructor for CommandHelp.</p>
     *
     * @param logger a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    public CommandHelp(Logger logger) {
        super(logger);
        CommandInterface.allowAllGroups(this);
    }

    /** {@inheritDoc} */
    @Override
    public String getHelpMessage() {
        return "You need help with help? That's kinda genius :)";
    }

    /** {@inheritDoc} */
    public void execute(String[] args, IShell shell) {

        if (args.length > 0) {
            for (CommandInterface cmd : CommandContainer.getCommands()) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    shell.getPrinter().println(cmd.getHelpMessage());
                    return;
                }
            }
        } else if (args.length > 1) {
            shell.getPrinter().println("Incremented help is not supported in this build.");
        } else {
            for (CommandInterface command : CommandContainer.getCommands()) {
                shell.getPrinter().println(command.getName() + ": \t\t\t\t" + command.getHelpMessage());
            }
        }

    }

    /**
     * <p>getName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getName() {
        return "help";
    }
}
