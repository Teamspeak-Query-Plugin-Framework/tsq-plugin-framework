package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.exceptions.*;

import java.util.Scanner;

public class CommandAddUser extends CommandInterface {

    private UserManager userManager;

    public CommandAddUser(Logger logger, ConsoleHandler consoleHandler) {
        super(logger);
        this.userManager = consoleHandler.getUserManager();
    }

    @Override
    public String getHelpMessage() {
        return "Adds a new user account";
    }

    @Override
    public void gotCalled(String[] args) {

        UserGroup newGroup;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        if (username.contains(" ") || username.isEmpty()) {
            System.out.println("Username must not contain whitespaces, please try again.");
            return;
        }
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();
        System.out.print("Re-type new password: ");
        String retypePassword = scanner.nextLine();
        if (password.equals(retypePassword)) {
            System.out.println("Passwords don't match, please try again.");
            return;
        }

        System.out.print("Group (Administrator / Guest): ");
        String group = scanner.nextLine();
        if (group.equalsIgnoreCase("ADMINISTRATOR"))
            newGroup = UserGroup.ADMINISTRATOR;
        else if (group.equalsIgnoreCase("GUEST"))
            newGroup = UserGroup.GUEST;
        else {
            System.out.print(group + " does not match any of the available groups, please try again.");
            return;
        }

        try {
            userManager.createUser(username, password, newGroup);
            System.out.println("New user created.");
        } catch (UserAlreadyExistingException e) {
            System.out.println("Fatal: User " + username + " already exists.");
            return;
        }

    }

    @Override
    public String getName() {
        return "adduser";
    }

}
