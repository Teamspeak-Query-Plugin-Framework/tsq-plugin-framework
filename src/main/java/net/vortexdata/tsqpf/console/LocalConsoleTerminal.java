package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.authenticator.User;

import java.util.Scanner;

public class LocalConsoleTerminal implements VirtualTerminal {

    private Logger logger;
    private Scanner scanner;
    private LocalConsole localConsole;

    public LocalConsoleTerminal(User user, LocalConsole localConsole) {
        logger = Framework.getInstance().getLogger();
        scanner = new Scanner(System.in);
        this.localConsole = localConsole;
    }

    @Override
    public void println(String msg) {
        System.out.println(msg);
    }

    public void print(String msg) {
        System.out.print(msg);
    }

    @Override
    public String readln() {
        return scanner.nextLine();
    }

    @Override
    public void logout() {
        localConsole.logout();
    }

}
