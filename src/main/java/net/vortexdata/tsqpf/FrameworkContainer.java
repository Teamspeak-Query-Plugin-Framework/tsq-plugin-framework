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
import net.vortexdata.tsqpf.configs.*;
import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.framework.*;
import net.vortexdata.tsqpf.listeners.*;
import net.vortexdata.tsqpf.modules.boothandler.*;
import net.vortexdata.tsqpf.plugins.*;
import org.apache.log4j.*;
import org.apache.log4j.Logger;

import java.util.*;

public class FrameworkContainer {

    private Framework framework;

    // Global Variables
    private HashMap<String, Boolean> booleanParameters;
    private Logger rootLogger;
    private final BootHandler bootHandler;

    // Teamspeak API
    private TS3Api ts3Api;
    private TS3Config ts3Config;
    private TS3Query ts3Query;

    // CLI
    private LocalShell localShell;
    private UserManager userManager;

    // Framework
    private ArrayList<Config> frameworkConfigs;
    private ChatCommandListener frameworkChatCommandListener;
    private PluginManager frameworkPluginManager;
    private net.vortexdata.tsqpf.console.Logger frameworkLogger;
    private ReconnectStrategy frameworkReconnectStrategy;
    private FrameworkStatus frameworkStatus;
    private LocalShell frameworkLocalShell;
    private String[] frameworkStartParameters;

    public FrameworkContainer(Framework framework, String[] args) {

        this.frameworkStartParameters = args;
        this.framework = framework;
        rootLogger =  LogManager.getRootLogger();

        booleanParameters = new HashMap<>();
        frameworkConfigs = new ArrayList<>();
        frameworkStatus = FrameworkStatus.STARTING;
        bootHandler = new BootHandler();
        bootHandler.setBootStartTime();

    }

    public void init() {
        frameworkLogger = new FrameworkLogger(framework);
        frameworkLogger.printInfo("Hi");

        parseArgs(frameworkStartParameters);

        frameworkLocalShell = new LocalShell(frameworkLogger, booleanParameters.get("-reset-root"));
        frameworkPluginManager = new PluginManager(this);

        loadConfigs();
    }

    public TS3Config generateTs3Config() {

        TS3Config ts3config = new TS3Config();

        frameworkLogger.printDebug("Trying to assign server address...");
        ts3config.setHost(getConfig("configs//main.properties").getProperty("serverAddress"));
        frameworkLogger.printDebug("Server address assigned.");

        frameworkLogger.printDebug("Trying to assign reconnect strategy...");
        String reconnectStrategy = getConfig("configs//main.properties").getProperty("reconnectStrategy");
        if (reconnectStrategy.equalsIgnoreCase("exponentialBackoff") || reconnectStrategy.equalsIgnoreCase("") || reconnectStrategy.isEmpty()) {
            ts3config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            this.frameworkReconnectStrategy = ReconnectStrategy.exponentialBackoff();
        } else if (reconnectStrategy.equalsIgnoreCase("disconnect")) {
            ts3config.setReconnectStrategy(ReconnectStrategy.disconnect());
            this.frameworkReconnectStrategy = ReconnectStrategy.disconnect();
        } else if (reconnectStrategy.equalsIgnoreCase("linearBackoff")) {
            ts3config.setReconnectStrategy(ReconnectStrategy.linearBackoff());
            this.frameworkReconnectStrategy = ReconnectStrategy.linearBackoff();
        } else if (reconnectStrategy.equalsIgnoreCase("userControlled")) {
            frameworkLogger.printWarn("UserControlled reconnect strategy is currently not supported, reverting to disconnect. You will have to manually restart the framework after a timeout.");
            ts3config.setReconnectStrategy(ReconnectStrategy.disconnect());
            this.frameworkReconnectStrategy = ReconnectStrategy.disconnect();
        } else {
            frameworkLogger.printWarn("Could not identify reconnect strategy " + reconnectStrategy + ", falling back to exponentialBackoff.");
            ts3config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            this.frameworkReconnectStrategy = ReconnectStrategy.exponentialBackoff();
        }
        frameworkLogger.printDebug("Reconnect strategy assigned.");

        ts3config.setConnectionHandler(new ConnectionHandler() {

            @Override
            public void onConnect(TS3Query ts3Query) {
                ts3Api = ts3Query.getApi();
                framework.wakeup(ts3Query);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                framework.hibernate();
            }

        });

        return ts3config;

    }

    public void loadConfigs() {

        // Register configs
        ConfigMain configMain = new ConfigMain();
        boolean didConfigMainExist = configMain.load();
        frameworkConfigs.add(configMain);

        ConfigMessages configMessages = new ConfigMessages();
        boolean didConfigMessagesExist = configMessages.load();
        frameworkConfigs.add(configMessages);

        if (!didConfigMessagesExist)
            frameworkLogger.printWarn("Could not find message config file, therefor created a new one. You might want to review its values.");
        if (!didConfigMainExist) {
            frameworkLogger.printWarn("Could not find config file, therefor created a new one. Please review and adjust its values to avoid any issues.");
            framework.shutdown();
        }
        frameworkLogger.printInfo("Configs registered and loaded.");

    }

    public Config getConfig(String path) {
        for (Config c : frameworkConfigs) {
            if (c.getPath().equals(path))
                return c;
        }

        return null;
    }

    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    public void setFrameworkLocalShell(LocalShell frameworkLocalShell) {
        this.frameworkLocalShell = frameworkLocalShell;
    }

    public HashMap<String, Boolean> getBooleanParameters() {
        return booleanParameters;
    }

    public Logger getRootLogger() {
        return rootLogger;
    }

    public TS3Api getTs3Api() {
        return ts3Api;
    }

    public TS3Config getTs3Config() {
        return ts3Config;
    }

    public LocalShell getLocalShell() {
        return localShell;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ChatCommandListener getFrameworkChatCommandListener() {
        return frameworkChatCommandListener;
    }

    public PluginManager getFrameworkPluginManager() {
        return frameworkPluginManager;
    }

    public net.vortexdata.tsqpf.console.Logger getFrameworkLogger() {
        return frameworkLogger;
    }

    public ReconnectStrategy getFrameworkReconnectStrategy() {
        return frameworkReconnectStrategy;
    }

    public FrameworkStatus getFrameworkStatus() {
        return frameworkStatus;
    }

    public Framework getFramework() {
        return framework;
    }

    public BootHandler getBootHandler() {
        return bootHandler;
    }

    public LocalShell getFrameworkLocalShell() {
        return frameworkLocalShell;
    }

    public void setBooleanParameters(HashMap<String, Boolean> booleanParameters) {
        this.booleanParameters = booleanParameters;
    }

    public void setTs3Api(TS3Api ts3Api) {
        this.ts3Api = ts3Api;
    }

    public void setTs3Config(TS3Config ts3Config) {
        this.ts3Config = ts3Config;
    }

    public void setLocalShell(LocalShell localShell) {
        this.localShell = localShell;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setFrameworkChatCommandListener(ChatCommandListener frameworkChatCommandListener) {
        this.frameworkChatCommandListener = frameworkChatCommandListener;
    }

    public void setFrameworkPluginManager(PluginManager frameworkPluginManager) {
        this.frameworkPluginManager = frameworkPluginManager;
    }

    public void setFrameworkLogger(net.vortexdata.tsqpf.console.Logger frameworkLogger) {
        this.frameworkLogger = frameworkLogger;
    }

    public void setFrameworkReconnectStrategy(ReconnectStrategy frameworkReconnectStrategy) {
        this.frameworkReconnectStrategy = frameworkReconnectStrategy;
    }

    public void setFrameworkStatus(FrameworkStatus frameworkStatus) {
        this.frameworkStatus = frameworkStatus;
    }

    public void addBooleanParameter(String key, Boolean value) {
        booleanParameters.put(key, value);
    }

    public boolean getBooleanParameter(String key) {
        if (booleanParameters.get(key) != null)
            return booleanParameters.get(key);
        else
            return false;
    }

    public TS3Query getTs3Query() {
        return ts3Query;
    }

    public ArrayList<Config> getFrameworkConfigs() {
        return frameworkConfigs;
    }

    public void setTs3Query(TS3Query ts3Query) {
        this.ts3Query = ts3Query;
    }

    public void setFrameworkConfigs(ArrayList<Config> frameworkConfigs) {
        this.frameworkConfigs = frameworkConfigs;
    }

    public void parseArgs(String[] args) {

        for (int i = 0; i < args.length; i++) {

            if (args[i].contains("-debug")) {
                rootLogger.setLevel(Level.DEBUG);
            }

            else if (args[i].contains("-setup")) {
                System.out.println("Setup wizard is not supported in this build.");
            }

            else if (args[i].contains("-reset-root")) {
                booleanParameters.put("-reset-root", true);
            }

        }

    }
}