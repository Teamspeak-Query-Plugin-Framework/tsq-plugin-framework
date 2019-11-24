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
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;

import java.io.PrintStream;
import java.util.Scanner;

/**
 * Interface for shell actions.
 * Using this as a type could be useful for testing.
 * @author Fabian Gurtner (fabian@profiluefter.me)
 */
public interface IShell {
	/**
	 * Authenticates once and executes commands until {@link IShell#logout()} is called.
	 * @return false if authentication failed
	 */
	boolean execute();

	//The 5 methods below should be private, but this is impossible before Java 9.
	/**
	 * Displays a prompt to authenticate and submits the input to {@link UserManager#authenticate(String, String)}.
	 * @return Returns the user that is authenticated.
	 */
	User authenticate();
	/**
	 * Prints the prompt that is suitable for the current user.
	 */
	void printPrompt();
	/**
	 * Reads a command and splits the arguments.
	 * @return An String[] containing the command name and args.
	 */
	String[] readCommand();
	/**
	 * Checks if the user still has the permission to execute the command.
	 * @param command The command in question.
	 * @return If the execution of the command is permitted.
	 * @throws UserNotFoundException In the edge case if the user was deleted while logged in this is thrown.
	 */
	boolean checkPermissions(CommandInterface command) throws UserNotFoundException;
	/**
	 * Executes the command.
	 * @param command The command to be executed.
	 * @param rawArgs An array including the command name and args.
	 */
	void executeCommand(CommandInterface command, String[] rawArgs);

	/**
	 * Logs the user out if called.
	 */
	void logout();

	PrintStream getPrinter();

	Scanner getReader();
}
