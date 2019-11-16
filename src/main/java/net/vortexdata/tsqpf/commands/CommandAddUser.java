package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.ConsoleCommandHandler;
import net.vortexdata.tsqpf.console.LocalConsole;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;
import net.vortexdata.tsqpf.exceptions.UserAlreadyExistingException;

public class CommandAddUser extends CommandInterface {

    private UserManager userManager;

    public CommandAddUser(Logger logger, UserManager userManager) {
        super(logger);
        this.userManager = userManager;
        groups.add(UserGroup.ROOT);
        groups.add(UserGroup.ADMINISTRATOR);
    }

    @Override
    public String getHelpMessage() {
        return "Adds a new user account";
    }

    @Override
    public void gotCalled(String[] args, VirtualTerminal terminal) {

        UserGroup newGroup;
        terminal.print("Enter new username: ");
        String username = terminal.readln();
        if (username.contains(" ") || username.isEmpty()) {
            terminal.println("Username must not contain whitespaces, please try again.");
            return;
        }

        if (!username.matches("[a-zA-Z0-9]+")) {
            terminal.println("Username must only contain alphanumeric characters.");
            return;
        }


        terminal.print("Enter new password: ");
        String password = terminal.readln();
        terminal.print("Re-type new password: ");
        String retypePassword = terminal.readln();
        if (!password.equals(retypePassword)) {
            terminal.println("Passwords don't match, please try again.");
            return;
        }

        terminal.print("Group (Administrator / Guest): ");
        String group = terminal.readln();
        if (group.equalsIgnoreCase("ADMINISTRATOR"))
            newGroup = UserGroup.ADMINISTRATOR;
        else if (group.equalsIgnoreCase("GUEST"))
            newGroup = UserGroup.GUEST;
        else {
            terminal.println(group + " does not match any of the available groups, please try again.");
            return;
        }

        try {
            userManager.createUser(username, password, newGroup);
            terminal.println("New user created.");
        } catch (UserAlreadyExistingException e) {
            terminal.println("Fatal: User " + username + " already exists.");
            return;
        }

    }

    @Override
    public String getName() {
        return "adduser";
    }

}
