/*
 *
 *  Teamspeak Query Plugin Framework
 *
 *  Copyright (C) 2019 - 2020 VortexdataNET
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package net.vortexdata.tsqpf;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.*;
import net.vortexdata.tsqpf.authenticator.*;
import net.vortexdata.tsqpf.commands.*;
import net.vortexdata.tsqpf.configs.*;
import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.exceptions.*;
import net.vortexdata.tsqpf.modules.statusreporter.FrameworkStatus;
import net.vortexdata.tsqpf.listeners.ChatCommandListener;
import net.vortexdata.tsqpf.listeners.GlobalEventHandler;
import net.vortexdata.tsqpf.modules.eula.*;
import net.vortexdata.tsqpf.modules.update.UpdateFetcher;
import net.vortexdata.tsqpf.plugins.*;

import java.io.*;

public class Framework {

    private FrameworkContainer frameworkContainer;

    public Framework(String[] args) {

        frameworkContainer = new FrameworkContainer(this, args);

    }

    public void launch() {

        // Print Startup Head
        printCopyHeader();

        // init framework container
        frameworkContainer.init();
        frameworkContainer.setFrameworkStatus(FrameworkStatus.STARTING);

        // Check for update
        UpdateFetcher updateFetcher = new UpdateFetcher(frameworkContainer);
        updateFetcher.checkForUpdate();

        // Check EULA
        checkEula();

        // Load main config
        frameworkContainer.loadConfigs();

        // Create query
        frameworkContainer.setTs3Query(new TS3Query(frameworkContainer.generateTs3Config()));

        // Establish connection
        frameworkContainer.getFrameworkLogger().printDebug("Trying to connect to server...");
        try {
            frameworkContainer.getTs3Query().connect();
            frameworkContainer.getFrameworkLogger().printDebug("Connection to server established.");
        } catch (Exception exx) {
            frameworkContainer.getFrameworkLogger().printError("Connection to server failed, dumping error details: ");
            exx.printStackTrace();
            shutdown();
        }

        frameworkContainer.getFrameworkLogger().printDebug("Initializing console handler...");
        frameworkContainer.setUserManager(new UserManager(frameworkContainer.getFrameworkLogger()));
        frameworkContainer.setLocalShell(new LocalShell(frameworkContainer.getFrameworkLogger(), frameworkContainer.getBooleanParameter("-reset-root")));
        frameworkContainer.getFrameworkLogger().printDebug("Console handler loaded.");
        frameworkContainer.getFrameworkLogger().printDebug("Registering console commands...");

        CommandContainer.registerCommand(new CommandHelp(frameworkContainer.getFrameworkLogger()));
        CommandContainer.registerCommand(new CommandClear(frameworkContainer.getFrameworkLogger()));
        CommandContainer.registerCommand(new CommandLogout(frameworkContainer.getFrameworkLogger()));
        CommandContainer.registerCommand(new CommandAddUser(frameworkContainer.getFrameworkLogger(), frameworkContainer.getUserManager()));
        CommandContainer.registerCommand(new CommandDelUser(frameworkContainer.getFrameworkLogger(), frameworkContainer.getUserManager()));
        CommandContainer.registerCommand(new CommandFramework(frameworkContainer));
        CommandContainer.registerCommand(new CommandPlugins(frameworkContainer));
        frameworkContainer.getFrameworkLogger().printDebug("Console handler and console commands successfully initialized and registered.");
        frameworkContainer.getBootHandler().setBootEndTime();
        frameworkContainer.getFrameworkLogger().printInfo("Boot process finished.");
        frameworkContainer.getFrameworkLogger().printInfo("It took " + frameworkContainer.getBootHandler().getBootTime() + " milliseconds to start the framework and load plugins.");

        frameworkContainer.getLocalShell().start();

    }

    private void checkEula() {

        Eula eula = new Eula(frameworkContainer.getFrameworkLogger());
        try {
            eula.init();
        } catch (OutdatedEulaException e) {
            frameworkContainer.getFrameworkLogger().printError("Your eula was outdated and has been updated. Please review it and run the framework again. By running the framework again you accept all changes done to the new eula version.");
            shutdown();
        }

        try {
            if (!Boolean.parseBoolean(frameworkContainer.getConfig("configs//main.properties").getProperty("acceptEula"))) {
                frameworkContainer.getFrameworkLogger().printError("You have to accept the eula before running the framework. It is located in the frameworks root directory and can be accepted by changing the 'acceptEula' config value in the main.properties.");
                shutdown();
            }
        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to parse 'acceptEula' config value.");
            shutdown();
        }
    }

    private void printCopyHeader() {
        BufferedReader headBr = null;

        try {
            InputStream headIs = getClass().getResourceAsStream("/startuphead.txt");
            headBr = new BufferedReader(new InputStreamReader(headIs));
            while (headBr.ready()) {
                System.out.println(headBr.readLine());
            }
        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to print startup header. Your framework may be corrupted.");
        }
    }

    public void shutdown() {

        frameworkContainer.setFrameworkStatus(FrameworkStatus.STOPPING);

        frameworkContainer.getFrameworkLogger().printInfo("Shutting down for system halt.");

        if (frameworkContainer.getLocalShell() != null) {
            frameworkContainer.getFrameworkLogger().printDebug("Shutting down console handler...");
            frameworkContainer.getLocalShell().shutdown();
        }


        if (frameworkContainer.getFrameworkPluginManager() != null) {
            frameworkContainer.getFrameworkLogger().printInfo("Unloading plugins...");
            frameworkContainer.getFrameworkPluginManager().disableAll();
        }

        frameworkContainer.getFrameworkLogger().printInfo("Successfully unloaded plugins and disabled console handler.");
        frameworkContainer.getFrameworkLogger().printInfo("Ending framework logging...");
        System.exit(0);
    }

    public void wakeup(TS3Query ts3Query) {

        frameworkContainer.setFrameworkStatus(FrameworkStatus.WAKING);
        frameworkContainer.getFrameworkLogger().printDebug("Wakeup initiated...");
        frameworkContainer.setTs3Api(ts3Query.getApi());
        frameworkContainer.getFrameworkLogger().printDebug("Trying to sign into query...");
        try {
            frameworkContainer.getTs3Api().login(
                    frameworkContainer.getConfig(new ConfigMain().getPath()).getProperty("queryUser"),
                    frameworkContainer.getConfig(new ConfigMain().getPath()).getProperty("queryPassword")
            );
        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to sign into query, dumping error details: ");
            e.printStackTrace();
            shutdown();
        }

        // Select virtual host
        frameworkContainer.getFrameworkLogger().printDebug("Trying to select virtual server...");
        try {
            frameworkContainer.getTs3Api().selectVirtualServerById(Integer.parseInt(frameworkContainer.getConfig("configs//main.properties").getProperty("virtualServerId")));
            frameworkContainer.getFrameworkLogger().printInfo("Successfully selected virtual server.");
        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to select virtual server, dumping error details: ", e);
            shutdown();
        }

        try {
            frameworkContainer.getFrameworkLogger().printDebug("Trying to assign nickname...");
            frameworkContainer.getTs3Api().setNickname(frameworkContainer.getConfig("configs//main.properties").getProperty("clientNickname"));
            frameworkContainer.getFrameworkLogger().printDebug("Successfully set nickname.");
        } catch (Exception e) {
            frameworkContainer.getFrameworkLogger().printError("Failed to set nickname, dumping error details: ", e);
            shutdown();
        }

        // Old Listeners and Handlers could cause unwanted side effects when reconnecting.

        frameworkContainer.getFrameworkLogger().printDebug("Starting up ChatCommandListener.");
        //TODO: Implement reuseable ChatCommandListener
        frameworkContainer.setFrameworkChatCommandListener(new ChatCommandListener(frameworkContainer));

        frameworkContainer.getFrameworkLogger().printDebug("Trying to register global events...");
        frameworkContainer.getTs3Api().registerAllEvents();
        //TODO: Implement reuseable GlobalEventHandler
        frameworkContainer.getTs3Api().addTS3Listeners(new GlobalEventHandler(frameworkContainer));
        frameworkContainer.getFrameworkLogger().printDebug("Successfully registered global events.");


        frameworkContainer.getFrameworkLogger().printDebug("Initializing plugin controller...");
        //TODO: Implement reuseable PluginManager
        frameworkContainer.setFrameworkPluginManager(new PluginManager(frameworkContainer));
        frameworkContainer.getFrameworkLogger().printDebug("Loading and enabling plugins...");
        frameworkContainer.getFrameworkPluginManager().enableAll();
        frameworkContainer.getFrameworkLogger().printDebug("Successfully loaded plugins.");

        frameworkContainer.setFrameworkStatus(FrameworkStatus.RUNNING);

    }

    public void hibernate() {
        frameworkContainer.setFrameworkStatus(FrameworkStatus.HIBERNATING);

        if (frameworkContainer.getFrameworkReconnectStrategy() == ReconnectStrategy.disconnect()) {
            shutdown();
            return;
        }
        frameworkContainer.getFrameworkLogger().printDebug("Hibernation initiated.");
        frameworkContainer.getFrameworkLogger().printDebug("Disabling all plugins...");
        frameworkContainer.getFrameworkPluginManager().disableAll();
        frameworkContainer.getFrameworkLogger().printDebug("All plugins disabled.");

        frameworkContainer.setLocalShell(null);

    }

    public FrameworkContainer getFrameworkContainer() {
        return frameworkContainer;
    }

}
