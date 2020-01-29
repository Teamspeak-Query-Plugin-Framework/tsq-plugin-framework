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

import net.vortexdata.tsqpf.commands.CommandInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all commands.
 *
 * @author Michael Wiesinger
 * @since 2.0.0
 * @version $Id: $Id
 */
public class CommandContainer {
	private static Map<String, CommandInterface> commands = new HashMap<>();

	/**
	 * Registers command to command container.
	 *
	 * @param command a {@link net.vortexdata.tsqpf.commands.CommandInterface} object.
	 * @return true if command has not been assigned yet.
	 */
	public static boolean registerCommand(CommandInterface command) {
		if(commands.containsKey(command.getName()))
			return false;

		commands.put(command.getName(), command);
		return true;
	}

	/**
	 *
	 * Searches for a command and returns one if found.
	 *
	 * @param name		The name of the command that should be returned.
	 * @return a {@link net.vortexdata.tsqpf.commands.CommandInterface} object.
	 */
	public static CommandInterface searchCommand(String name) {
		return commands.get(name);
	}

	/**
	 * <p>reset.</p>
	 */
	public static void reset() {
		commands.clear();
	}

	/**
	 * <p>Getter for the field <code>commands</code>.</p>
	 *
	 * @return an array of {@link net.vortexdata.tsqpf.commands.CommandInterface} objects.
	 */
	public static CommandInterface[] getCommands() {
		return commands.values().toArray(new CommandInterface[0]);
	}
}
