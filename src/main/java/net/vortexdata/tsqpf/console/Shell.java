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
 *
 * @author Fabian Gurtner (fabian@profiluefter.me)
 * @since 2.0.0
 * @version $Id: $Id
 */
public class Shell implements IShell {
	protected Logger logger;
	protected String clientHostname;
	protected Scanner scanner;
	protected PrintStream printer;
	protected User user;
	protected UserManager userManager;

	/** Constant <code>delimiter="\n"</code> */
	protected static final String delimiter = "\n";

	//Custom

	/**
	 * <p>Constructor for Shell.</p>
	 *
	 * @param clientHostname The hostname/ip of the client. Shown in the prompt.
	 * @param shellInputStream The {@link java.io.InputStream} used to communicate with the client.
	 * @param shellOutputStream The {@link java.io.OutputStream} used to communicate with the client.
	 * @param logger Logger that is used for one warning and the {@link net.vortexdata.tsqpf.authenticator.UserManager#UserManager(Logger)}
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
	 * {@inheritDoc}
	 *
	 * Authenticates once and executes commands until {@link IShell#logout()} is called.
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
			if (command == null) {
				if (!rawCommand[0].isEmpty())
					printer.println(rawCommand[0] + ": command not found");
				return false;
			}
			boolean hasPermission;
			try {
				hasPermission = checkPermissions(command);
			} catch(UserNotFoundException e) {
				if (!rawCommand[0].isEmpty())
					printer.println("User has been deleted before executing command!");
				break;
			}
			if(hasPermission)
				executeCommand(command, rawCommand);
			else
				printer.println("Insufficient permissions!");
		}

		return true;
	}

	//IShell

	/**
	 * {@inheritDoc}
	 *
	 * Displays a prompt to authenticate and submits the input to {@link UserManager#authenticate(String, String)}.
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
	 * {@inheritDoc}
	 *
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
	 * {@inheritDoc}
	 *
	 * Reads a command and splits the arguments.
	 */
	@Override
	public String[] readCommand() {
		return scanner.nextLine().split(" ");
	}

	/**
	 * {@inheritDoc}
	 *
	 * Checks if the user still has the permission to execute the command.
	 */
	@Override
	public boolean checkPermissions(CommandInterface command) throws UserNotFoundException {
		if (command == null)
			return false;
		userManager.reloadUsers();
		return command.isGroupRequirementMet(user.getGroup());
	}

	/**
	 * {@inheritDoc}
	 *
	 * Executes the command.
	 */
	@Override
	public void executeCommand(CommandInterface command, String[] rawArgs) {
		command.execute(Arrays.copyOfRange(rawArgs, 1, rawArgs.length),this);
	}

	/**
	 * {@inheritDoc}
	 *
	 * Logs the user out.
	 */
	@Override
	public void logout() {
		user = null;
	}

	/** {@inheritDoc} */
	@Override
	public PrintStream getPrinter() {
		return this.printer;
	}

	/** {@inheritDoc} */
	@Override
	public Scanner getReader() {
		return this.scanner;
	}
}
