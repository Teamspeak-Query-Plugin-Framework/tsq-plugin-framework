package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;
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
    public void execute(String[] args, IShell shell) {

        UserGroup newGroup;
        shell.getPrinter().print("Enter new username: ");
        String username = shell.getReader().nextLine();
        if (username.contains(" ") || username.isEmpty()) {
            shell.getPrinter().println("Username must not contain whitespaces, please try again.");
            return;
        }

        if (!username.matches("[a-zA-Z0-9]+")) {
            shell.getPrinter().println("Username must only contain alphanumeric characters.");
            return;
        }


        shell.getPrinter().print("Enter new password: ");
        String password = shell.getReader().nextLine();
        shell.getPrinter().print("Re-type new password: ");
        String retypePassword = shell.getReader().nextLine();
        if (!password.equals(retypePassword)) {
            shell.getPrinter().println("Passwords don't match, please try again.");
            return;
        }

        shell.getPrinter().print("Group (Administrator / Guest): ");
        String group = shell.getReader().nextLine();
        if (group.equalsIgnoreCase("ADMINISTRATOR"))
            newGroup = UserGroup.ADMINISTRATOR;
        else if (group.equalsIgnoreCase("GUEST"))
            newGroup = UserGroup.GUEST;
        else {
            shell.getPrinter().println(group + " does not match any of the available groups, please try again.");
            return;
        }

        try {
            userManager.createUser(username, password, newGroup);
            shell.getPrinter().println("New user created.");
        } catch (UserAlreadyExistingException e) {
            shell.getPrinter().println("Fatal: User " + username + " already exists.");
            return;
        }

    }

    @Override
    public String getName() {
        return "adduser";
    }

}
