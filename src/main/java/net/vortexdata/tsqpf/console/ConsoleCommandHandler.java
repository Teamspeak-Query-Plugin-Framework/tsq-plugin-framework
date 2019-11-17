package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.commands.CommandInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Deprecated
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

    public List<CommandInterface> getCommands() {
        return Collections.unmodifiableList(commands);
    }

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
