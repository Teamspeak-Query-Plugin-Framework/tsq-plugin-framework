package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;

//TODO: Documentation
public interface IShell {
	boolean run();

	User authenticate();
	void printPrompt();
	String[] readCommand();
	boolean checkPermissions(CommandInterface command) throws UserNotFoundException;
	void executeCommand(CommandInterface command, String[] rawArgs);

	void logout();
}
