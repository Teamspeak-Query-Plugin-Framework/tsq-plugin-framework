package net.vortexdata.tsqpf.installers;

import net.vortexdata.tsqpf.configs.ConfigMain;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Simple user interface to assist users in configuring the framework.
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class InstallWizzard {

    private HashMap<String, String> values;

    public InstallWizzard() {
        ConfigMain cMain = new ConfigMain();
        values = cMain.getDefaultValues();
    }

    /**
     * Call the interface
     */
    public void init() {
        values = new HashMap<String, String>();
        Scanner scanner = new Scanner(System.in);
        System.out.println("You've started the program in setup mode.");
        System.out.println("-----------------------------------------");
        System.out.println("The address of your server can be an IP address or domain (e.g. 'vortexdata.net' or '127.0.0.1'). If your Teamspeak server runs on the same system as the bot, your can enter '127.0.0.1'.");
        System.out.print("Your servers address: ");
        values.put("serverAddress", scanner.nextLine());

        boolean isVirtualIdValid = false;
        do {
            System.out.println("\nThe virtual server ID is the ID of the virtual server you want to run this bot on. If you only have one virtual server instance, put in '1' as default.");
            System.out.print("Your virtual server id: ");
            String paramVirtualServerIdString = scanner.nextLine();

            try {
                int paramVirtualServerId = Integer.parseInt(paramVirtualServerIdString);
                values.put("virtualServer", paramVirtualServerIdString);
                isVirtualIdValid = true;
            } catch (Exception e) {
                System.out.println("\nInvalid input!");
            }
        } while (!isVirtualIdValid);

        boolean isPortValid = false;
        do {
            System.out.println("\nThe server query port specifies on which port this bot will connect to your query. It's default value is '10011'.");
            System.out.print("Query port: ");
            String paramQueryPortString = scanner.nextLine();

            try {
                int paramQueryPort = Integer.parseInt(paramQueryPortString);
                values.put("queryPort", paramQueryPortString);
                isPortValid = true;
            } catch (Exception e) {
                System.out.println("\nInvalid input!");
            }
        } while (!isPortValid);

        System.out.println("\nQuery user is used to log into query. Default 'serveradmin'.");
        System.out.print("Query user: ");
        values.put("queryUser", scanner.nextLine());

        System.out.println("\nQuery password is used to log into query. Default value is generated on first successful launch of your Teamspeak server.");
        System.out.print("Query password: ");
        values.put("queryPassword", scanner.nextLine());

        System.out.println("\nThe bots nickname will be displayed in chat tabs etc.");
        System.out.print("Framework nickname: ");
        values.put("botNickname", scanner.nextLine());

        InstallConfig installConfig = new InstallConfig();
        installConfig.create("configs/main.properties", values);
    }

}