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

//TODO: Documentation
public class Shell implements VirtualTerminal, IShell {
	private Logger logger;
	private String clientHostname;
	private Scanner scanner;
	private PrintStream printer;
	private User user;
	private UserManager userManager;

	private static final String delimiter = "\n";

	//Custom

	public Shell(String clientHostname, InputStream shellInputStream, OutputStream shellOutputStream, Logger logger) {
		this.clientHostname = clientHostname;

		this.scanner = new Scanner(shellInputStream);
		this.scanner.useDelimiter(delimiter);

		this.printer = new PrintStream(shellOutputStream);

		this.userManager = new UserManager(logger);

		this.logger = logger;
	}

	//Returns false if never authenticated
	@Override
	public boolean run() {
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

	@Override
	public void printPrompt() {
		if(user == null) {
			logger.printWarn("Tried to print shell prompt without an authenticated user!", new IllegalStateException());
			return;
		}
		printer.format("%s@%s> ", user.getUsername(), clientHostname);
	}

	@Override
	public String[] readCommand() {
		return scanner.nextLine().split(" ");
	}

	@Override
	public boolean checkPermissions(CommandInterface command) throws UserNotFoundException {
		userManager.reloadUsers();
		return command.isGroupRequirementMet(userManager.getUser(user.getUsername()).getGroup());
	}

	@Override
	public void executeCommand(CommandInterface command, String[] rawArgs) {
		command.gotCalled(Arrays.copyOfRange(rawArgs, 1, rawArgs.length),this);
	}

	//TODO: Rewrite command interface to support this
	@Override
	public void logout() {
		user = null;
	}

	//VirtualTerminal
	//TODO: Make VirtualTerminal deprecated

	@Override
	public void println(String msg) {
		this.printer.println(msg);
	}

	@Override
	public void print(String msg) {
		this.printer.print(msg);
	}

	@Override
	public String readln() {
		return scanner.nextLine();
	}
}
