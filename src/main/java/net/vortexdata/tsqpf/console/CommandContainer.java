package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.commands.CommandInterface;

import java.util.HashMap;
import java.util.Map;

//TODO: Documentation
public class CommandContainer {
	private static Map<String, CommandInterface> commands = new HashMap<>();

	public static boolean registerCommand(CommandInterface command) {
		if(commands.containsKey(command.getName()))
			return false;

		commands.put(command.getName(), command);
		return true;
	}

	public static CommandInterface searchCommand(String name) {
		return commands.get(name);
	}

	public static CommandInterface[] getCommands() {
		return commands.values().toArray(new CommandInterface[0]);
	}
}
