package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.Framework;

import java.util.Scanner;

public class ConsoleTerminal implements VirtualTerminal {

    private Logger logger;
    private Scanner scanner;

    public ConsoleTerminal() {
        logger = Framework.getInstance().getLogger();
        scanner = new Scanner(System.in);
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

}
