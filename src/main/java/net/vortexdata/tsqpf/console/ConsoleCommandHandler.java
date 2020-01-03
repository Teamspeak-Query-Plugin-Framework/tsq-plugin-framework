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

package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.commands.CommandInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
/**
 * <p>ConsoleCommandHandler class.</p>
 *
 * @author TAXSET
 * @version $Id: $Id
 */
public class ConsoleCommandHandler {




    private List<CommandInterface> commands = Collections.synchronizedList(new ArrayList<CommandInterface>());
    /**
     * Registers a console command.
     *
     * @param cmd Class of command to register
     * @return True if command was successfully registered
     */
    public boolean registerCommand(CommandInterface cmd) {
        for (CommandInterface c : commands)
            if (c.getName().equalsIgnoreCase(cmd.getName())) return false;

        commands.add(cmd);
        return true;
    }

    /**
     * <p>Getter for the field <code>commands</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<CommandInterface> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    /**
     * <p>processInput.</p>
     *
     * @param line a {@link java.lang.String} object.
     * @param user a {@link net.vortexdata.tsqpf.authenticator.User} object.
     * @param terminal a {@link net.vortexdata.tsqpf.console.VirtualTerminal} object.
     */
    public void processInput(String line, User user, VirtualTerminal terminal) {

        String[] data = line.split(" ");
        String commandPrefix = data[0];

        if (data.length > 0 && !data[0].isEmpty()) {
            boolean commandExists = false;
            for (CommandInterface cmd : commands) {
                if (cmd.getName().equalsIgnoreCase(data[0])) {
                    if (cmd.getGroupRange() == 0 || cmd.isGroupRequirementMet(user.getGroup())) {
                        //TODO: cmd.execute(Arrays.copyOfRange(data, 1, data.length), terminal);
                        commandExists = true;
                        break;
                    } else {
                        terminal.println("Permission denied.");
                    }
                    commandExists = true;
                }
            }
            if (!commandExists) {
                terminal.println(commandPrefix + ": command not found");
            }
        }
    }
}
