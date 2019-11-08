package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.exceptions.UserNotFoundException;

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
	boolean run();

	//TODO: The 5 methods below should be private, but this is impossible before Java 9.
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
}
