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
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Shell implementation
 * @author Fabian Gurtner (fabian@profiluefter.me)
 */
public class Shell implements IShell {
	protected Logger logger;
	protected String clientHostname;
	protected Scanner scanner;
	protected PrintStream printer;
	protected User user;
	protected UserManager userManager;

	protected static final String delimiter = "\n";

	//Custom

	/**
	 * @param clientHostname The hostname/ip of the client. Shown in the prompt.
	 * @param shellInputStream The {@link InputStream} used to communicate with the client.
	 * @param shellOutputStream The {@link OutputStream} used to communicate with the client.
	 * @param logger Logger that is used for one warning and the {@link UserManager#UserManager(Logger)}
	 */
	public Shell(String clientHostname, InputStream shellInputStream, OutputStream shellOutputStream, Logger logger) {
		this.clientHostname = clientHostname;

		this.scanner = new Scanner(shellInputStream);
		this.scanner.useDelimiter(delimiter);

		this.printer = new PrintStream(shellOutputStream);

		this.userManager = new UserManager(logger);

		this.logger = logger;
	}

	/**
	 * Authenticates once and executes commands until {@link IShell#logout()} is called.
	 * @return false if authentication failed
	 */
	@Override
	public boolean execute() {
		user = authenticate();
		if(user == null)
			return false;

		while(user != null) {
			printPrompt();
			String[] rawCommand = readCommand();
			if(rawCommand.length < 1) continue;
			CommandInterface command = CommandContainer.searchCommand(rawCommand[0]);
			boolean hasPermission;
			try {
				hasPermission = checkPermissions(command);
			} catch(UserNotFoundException e) {
				printer.println("User has been deleted before executing command!");
				break;
			}
			if(hasPermission)
				executeCommand(command, rawCommand);
			else
				printer.println("Permissions insufficient!");
		}

		return true;
	}

	//IShell

	/**
	 * Displays a prompt to authenticate and submits the input to {@link UserManager#authenticate(String, String)}.
	 * @return Returns the user that is authenticated.
	 */
	@Override
	public User authenticate() {
		if(user != null)
			return user;

		printer.println("Authentication required, please sign in to proceed.");
		printer.print("Username: ");
		String username = scanner.nextLine();
		printer.print("Password: ");
		String password = scanner.nextLine();

		try {
			User user = userManager.authenticate(username,password);
			printer.println("Sign in approved.");
			return user;
		} catch(InvalidCredentialsException e) {
			printer.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Prints the prompt that is suitable for the current user.
	 */
	@Override
	public void printPrompt() {
		if(user == null) {
			logger.printWarn("Tried to print shell prompt without an authenticated user!", new IllegalStateException());
			return;
		}
		printer.format("%s@%s> ", user.getUsername(), clientHostname);
	}

	/**
	 * Reads a command and splits the arguments.
	 * @return An String[] containing the command name and args.
	 */
	@Override
	public String[] readCommand() {
		return scanner.nextLine().split(" ");
	}

	/**
	 * Checks if the user still has the permission to execute the command.
	 * @param command The command in question.
	 * @return If the execution of the command is permitted.
	 * @throws UserNotFoundException In the edge case if the user was deleted while logged in this is thrown.
	 */
	@Override
	public boolean checkPermissions(CommandInterface command) throws UserNotFoundException {
		userManager.reloadUsers();
		return command.isGroupRequirementMet(userManager.getUser(user.getUsername()).getGroup());
	}

	/**
	 * Executes the command.
	 * @param command The command to be executed.
	 * @param rawArgs An array including the command name and args.
	 */
	@Override
	public void executeCommand(CommandInterface command, String[] rawArgs) {
		command.execute(Arrays.copyOfRange(rawArgs, 1, rawArgs.length),this);
	}

	/**
	 * Logs the user out.
	 */
	@Override
	public void logout() {
		user = null;
	}

	@Override
	public PrintStream getPrinter() {
		return this.printer;
	}

	@Override
	public Scanner getReader() {
		return this.scanner;
	}
}
