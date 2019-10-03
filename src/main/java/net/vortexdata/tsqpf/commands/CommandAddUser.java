package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.console.Logger;

import java.util.Scanner;

public class CommandAddUser extends CommandInterface {

    private UserManager userManager;

    public CommandAddUser(Logger logger, ConsoleHandler consoleHandler) {
        super(logger);
        groups.add(UserGroup.ROOT);
        this.userManager = consoleHandler.getUserManager();
    }

    @Override
    public String getHelpMessage() {
        return "Creates a new user";
    }

    @Override
    public void gotCalled(String[] args) {
        UserGroup newUserGroup;

        Scanner scanner = new Scanner(System.in);
        System.out.print("New username: ");
        String username = scanner.nextLine();
        System.out.print("New password: ");
        String password = scanner.nextLine();
        System.out.print("Group (Administrator | Guest): ");
        String group =  scanner.nextLine();
        if (group.equalsIgnoreCase(UserGroup.ADMINISTRATOR.toString()))
            newUserGroup = UserGroup.ADMINISTRATOR;
        else if (group.equalsIgnoreCase(UserGroup.GUEST.toString()))
            newUserGroup = UserGroup.GUEST;
        
    }

    @Override
    public String getName() {
        return "adduser";
    }
}
