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

package net.vortexdata.tsqpf.framework;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.*;
import net.vortexdata.tsqpf.authenticator.*;
import net.vortexdata.tsqpf.configs.*;
import net.vortexdata.tsqpf.console.*;
import net.vortexdata.tsqpf.listeners.*;
import net.vortexdata.tsqpf.modules.boothandler.*;
import net.vortexdata.tsqpf.modules.statusreporter.*;
import net.vortexdata.tsqpf.modules.uuid.UuidManager;
import net.vortexdata.tsqpf.plugins.*;
import net.vortexdata.tsqpf.utils.ResourceLoader;
import org.apache.log4j.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Contains all framework variables for easy use.
 *
 * @author Sandro Kierner (sandro@vortexdata.net)
 * @since 2.0.0
 * @version $Id: $Id
 */
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
    private StatusReporter frameworkStatusReporter;
    private UuidManager frameworkUuidManager;
    private GlobalEventHandler globalEventHandler;
    private ChatCommandListener chatCommandListener;
    private PluginManager pluginManager;
    private CommandContainer frameworkCommandContainer;

    // Framework Utils
    private ResourceLoader frameworkResourceLoader;

    /**
     * <p>Constructor for FrameworkContainer.</p>
     *
     * @param framework a {@link net.vortexdata.tsqpf.framework.Framework} object.
     * @param args an array of {@link java.lang.String} objects.
     */
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


    /**
     * <p>init.</p>
     */
    public void init() {

        frameworkLogger = new FrameworkLogger(framework);
        rootLogger.setLevel(Level.INFO);
        parseArgs(frameworkStartParameters);
        frameworkLocalShell = new LocalShell(frameworkLogger, getBooleanParameter("-reset-root"));
        frameworkPluginManager = new PluginManager(this);

        loadConfigs();

        // Init UUID Manager
        frameworkUuidManager = new UuidManager(this);
        frameworkUuidManager.init();

        // Init Framework Status Reporter
        this.frameworkStatusReporter = new StatusReporter(this);
        frameworkStatusReporter.logEvent(StatusEvents.STARTUP);

        globalEventHandler = new GlobalEventHandler(this);
        chatCommandListener = new ChatCommandListener(this);
        pluginManager = new PluginManager(this);





    }

    /**
     * <p>generateTs3Config.</p>
     *
     * @return a {@link com.github.theholywaffle.teamspeak3.TS3Config} object.
     */
    public TS3Config generateTs3Config() {

        TS3Config localTs3config = new TS3Config();

        frameworkLogger.printDebug("Trying to assign server address...");
        localTs3config.setHost(getConfig("configs//main.properties").getProperty("serverAddress"));
        frameworkLogger.printDebug("Server address assigned.");

        frameworkLogger.printDebug("Trying to assign reconnect strategy...");
        String reconnectStrategy = getConfig(new ConfigMain().getPath()).getProperty("reconnectStrategy");
        if (reconnectStrategy.equalsIgnoreCase("exponentialBackoff") || reconnectStrategy.equalsIgnoreCase("") || reconnectStrategy.isEmpty()) {
            localTs3config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            this.frameworkReconnectStrategy = ReconnectStrategy.exponentialBackoff();
            frameworkLogger.printInfo("Reconnect strategy set to exponentialBackoff.");
        } else if (reconnectStrategy.equalsIgnoreCase("disconnect")) {
            localTs3config.setReconnectStrategy(ReconnectStrategy.disconnect());
            this.frameworkReconnectStrategy = ReconnectStrategy.disconnect();
            frameworkLogger.printInfo("Reconnect strategy set to disconnect.");
        } else if (reconnectStrategy.equalsIgnoreCase("linearBackoff")) {
            localTs3config.setReconnectStrategy(ReconnectStrategy.linearBackoff());
            this.frameworkReconnectStrategy = ReconnectStrategy.linearBackoff();
            frameworkLogger.printInfo("Reconnect strategy set to linearBackoff.");
        } else if (reconnectStrategy.equalsIgnoreCase("userControlled")) {
            frameworkLogger.printWarn("UserControlled reconnect strategy is currently not supported, reverting to disconnect. You will have to manually restart the framework after a timeout.");
            localTs3config.setReconnectStrategy(ReconnectStrategy.disconnect());
            this.frameworkReconnectStrategy = ReconnectStrategy.disconnect();
            frameworkLogger.printInfo("Reconnect strategy set to disconnect (unsupported user controlled).");
        } else {
            frameworkLogger.printWarn("Could not identify reconnect strategy " + reconnectStrategy + ", falling back to exponentialBackoff.");
            localTs3config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            this.frameworkReconnectStrategy = ReconnectStrategy.exponentialBackoff();
        }

        frameworkLogger.printDebug("Registering reconnect strategy...");

        localTs3config.setConnectionHandler(new ConnectionHandler() {

            @Override
            public void onConnect(TS3Query ts3Query) {
                frameworkLogger.printDebug("Api onConnect event fired...");
                ts3Api = ts3Query.getApi();
                framework.wakeup(ts3Query);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                framework.hibernate();
            }

        });

        frameworkLogger.printDebug("Reconnect strategy registration finished.");

        this.ts3Config = localTs3config;

        return localTs3config;

    }

    /**
     * <p>loadConfigs.</p>
     */
    public void loadConfigs() {

        // Register configs
        ConfigMain configMain = new ConfigMain();
        boolean didConfigMainExist = configMain.load();
        frameworkConfigs.add(configMain);

        ConfigMessages configMessages = new ConfigMessages();
        boolean didConfigMessagesExist = configMessages.load();
        frameworkConfigs.add(configMessages);

        ConfigProject configProject = new ConfigProject();
        configProject.load();
        frameworkConfigs.add(configProject);

        if (!didConfigMessagesExist)
            frameworkLogger.printWarn("Could not find message config file, therefor created a new one. You might want to review its values.");
        if (!didConfigMainExist) {
            frameworkLogger.printWarn("Could not find config file, therefor created a new one. Please review and adjust its values to avoid any issues.");
            framework.shutdown();
        }
        frameworkLogger.printInfo("Configs registered and loaded.");

    }

    /**
     * <p>getConfig.</p>
     *
     * @param path a {@link java.lang.String} object.
     * @return a {@link net.vortexdata.tsqpf.configs.Config} object.
     */
    public Config getConfig(String path) {
        for (Config c : frameworkConfigs) {
            if (c.getPath().equals(path))
                return c;
        }

        return null;
    }

    /**
     * <p>Setter for the field <code>framework</code>.</p>
     *
     * @param framework a {@link net.vortexdata.tsqpf.framework.Framework} object.
     */
    public void setFramework(Framework framework) {
        this.framework = framework;
    }

    /**
     * <p>Setter for the field <code>frameworkLocalShell</code>.</p>
     *
     * @param frameworkLocalShell a {@link net.vortexdata.tsqpf.console.LocalShell} object.
     */
    public void setFrameworkLocalShell(LocalShell frameworkLocalShell) {
        this.frameworkLocalShell = frameworkLocalShell;
    }

    /**
     * <p>Getter for the field <code>booleanParameters</code>.</p>
     *
     * @return a {@link java.util.HashMap} object.
     */
    public HashMap<String, Boolean> getBooleanParameters() {
        return booleanParameters;
    }

    /**
     * <p>Getter for the field <code>rootLogger</code>.</p>
     *
     * @return a {@link org.apache.log4j.Logger} object.
     */
    public Logger getRootLogger() {
        return rootLogger;
    }

    /**
     * <p>Getter for the field <code>ts3Api</code>.</p>
     *
     * @return a {@link com.github.theholywaffle.teamspeak3.TS3Api} object.
     */
    public TS3Api getTs3Api() {
        return ts3Api;
    }

    /**
     * <p>Getter for the field <code>ts3Config</code>.</p>
     *
     * @return a {@link com.github.theholywaffle.teamspeak3.TS3Config} object.
     */
    public TS3Config getTs3Config() {
        return ts3Config;
    }

    /**
     * <p>Getter for the field <code>localShell</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.console.LocalShell} object.
     */
    public LocalShell getLocalShell() {
        return localShell;
    }

    /**
     * <p>Getter for the field <code>userManager</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.authenticator.UserManager} object.
     */
    public UserManager getUserManager() {
        return userManager;
    }

    /**
     * <p>Getter for the field <code>frameworkChatCommandListener</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.listeners.ChatCommandListener} object.
     */
    public ChatCommandListener getFrameworkChatCommandListener() {
        return frameworkChatCommandListener;
    }

    /**
     * <p>Getter for the field <code>frameworkPluginManager</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.plugins.PluginManager} object.
     */
    public PluginManager getFrameworkPluginManager() {
        return frameworkPluginManager;
    }

    /**
     * <p>Getter for the field <code>frameworkLogger</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    public net.vortexdata.tsqpf.console.Logger getFrameworkLogger() {
        return frameworkLogger;
    }

    /**
     * <p>Getter for the field <code>frameworkReconnectStrategy</code>.</p>
     *
     * @return a {@link com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy} object.
     */
    public ReconnectStrategy getFrameworkReconnectStrategy() {
        return frameworkReconnectStrategy;
    }

    /**
     * <p>Getter for the field <code>frameworkStatus</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.framework.FrameworkStatus} object.
     */
    public FrameworkStatus getFrameworkStatus() {
        return frameworkStatus;
    }

    /**
     * <p>Getter for the field <code>framework</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.framework.Framework} object.
     */
    public Framework getFramework() {
        return framework;
    }

    /**
     * <p>Getter for the field <code>bootHandler</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.modules.boothandler.BootHandler} object.
     */
    public BootHandler getBootHandler() {
        return bootHandler;
    }

    /**
     * <p>Getter for the field <code>frameworkLocalShell</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.console.LocalShell} object.
     */
    public LocalShell getFrameworkLocalShell() {
        return frameworkLocalShell;
    }

    /**
     * <p>Setter for the field <code>booleanParameters</code>.</p>
     *
     * @param booleanParameters a {@link java.util.HashMap} object.
     */
    public void setBooleanParameters(HashMap<String, Boolean> booleanParameters) {
        this.booleanParameters = booleanParameters;
    }

    /**
     * <p>Setter for the field <code>ts3Api</code>.</p>
     *
     * @param ts3Api a {@link com.github.theholywaffle.teamspeak3.TS3Api} object.
     */
    public void setTs3Api(TS3Api ts3Api) {
        this.ts3Api = ts3Api;
    }

    /**
     * <p>Setter for the field <code>ts3Config</code>.</p>
     *
     * @param ts3Config a {@link com.github.theholywaffle.teamspeak3.TS3Config} object.
     */
    public void setTs3Config(TS3Config ts3Config) {
        this.ts3Config = ts3Config;
    }

    /**
     * <p>Setter for the field <code>localShell</code>.</p>
     *
     * @param localShell a {@link net.vortexdata.tsqpf.console.LocalShell} object.
     */
    public void setLocalShell(LocalShell localShell) {
        this.localShell = localShell;
    }

    /**
     * <p>Setter for the field <code>userManager</code>.</p>
     *
     * @param userManager a {@link net.vortexdata.tsqpf.authenticator.UserManager} object.
     */
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    /**
     * <p>Setter for the field <code>frameworkChatCommandListener</code>.</p>
     *
     * @param frameworkChatCommandListener a {@link net.vortexdata.tsqpf.listeners.ChatCommandListener} object.
     */
    public void setFrameworkChatCommandListener(ChatCommandListener frameworkChatCommandListener) {
        this.frameworkChatCommandListener = frameworkChatCommandListener;
    }

    /**
     * <p>Setter for the field <code>frameworkPluginManager</code>.</p>
     *
     * @param frameworkPluginManager a {@link net.vortexdata.tsqpf.plugins.PluginManager} object.
     */
    public void setFrameworkPluginManager(PluginManager frameworkPluginManager) {
        this.frameworkPluginManager = frameworkPluginManager;
    }

    /**
     * <p>Setter for the field <code>frameworkLogger</code>.</p>
     *
     * @param frameworkLogger a {@link net.vortexdata.tsqpf.console.Logger} object.
     */
    public void setFrameworkLogger(net.vortexdata.tsqpf.console.Logger frameworkLogger) {
        this.frameworkLogger = frameworkLogger;
    }

    /**
     * <p>Setter for the field <code>frameworkReconnectStrategy</code>.</p>
     *
     * @param frameworkReconnectStrategy a {@link com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy} object.
     */
    public void setFrameworkReconnectStrategy(ReconnectStrategy frameworkReconnectStrategy) {
        this.frameworkReconnectStrategy = frameworkReconnectStrategy;
    }

    /**
     * <p>Setter for the field <code>frameworkStatus</code>.</p>
     *
     * @param frameworkStatus a {@link net.vortexdata.tsqpf.framework.FrameworkStatus} object.
     */
    public void setFrameworkStatus(FrameworkStatus frameworkStatus) {
        this.frameworkStatus = frameworkStatus;
    }

    /**
     * <p>addBooleanParameter.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @param value a {@link java.lang.Boolean} object.
     */
    public void addBooleanParameter(String key, Boolean value) {
        booleanParameters.put(key, value);
    }

    /**
     * <p>getBooleanParameter.</p>
     *
     * @param key a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean getBooleanParameter(String key) {
        if (booleanParameters.get(key) != null)
            return booleanParameters.get(key);
        else
            return false;
    }

    /**
     * <p>Getter for the field <code>ts3Query</code>.</p>
     *
     * @return a {@link com.github.theholywaffle.teamspeak3.TS3Query} object.
     */
    public TS3Query getTs3Query() {
        return ts3Query;
    }

    /**
     * <p>Getter for the field <code>frameworkConfigs</code>.</p>
     *
     * @return a {@link java.util.ArrayList} object.
     */
    public ArrayList<Config> getFrameworkConfigs() {
        return frameworkConfigs;
    }

    /**
     * <p>getFrameworkConfig.</p>
     *
     * @param configType a {@link net.vortexdata.tsqpf.configs.Config} object.
     * @return a {@link net.vortexdata.tsqpf.configs.Config} object.
     */
    public Config getFrameworkConfig(Config configType) {

        for (Config config : frameworkConfigs) {
            if (config.getClass().getGenericSuperclass().equals(configType.getClass().getGenericSuperclass())) {
                return config;
            }
        }

        return null;
    }

    /**
     * <p>Setter for the field <code>ts3Query</code>.</p>
     *
     * @param ts3Query a {@link com.github.theholywaffle.teamspeak3.TS3Query} object.
     */
    public void setTs3Query(TS3Query ts3Query) {
        this.ts3Query = ts3Query;
    }

    /**
     * <p>Setter for the field <code>frameworkConfigs</code>.</p>
     *
     * @param frameworkConfigs a {@link java.util.ArrayList} object.
     */
    public void setFrameworkConfigs(ArrayList<Config> frameworkConfigs) {
        this.frameworkConfigs = frameworkConfigs;
    }

    /**
     * <p>parseArgs.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
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

    /**
     * <p>Getter for the field <code>frameworkStartParameters</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getFrameworkStartParameters() {
        return frameworkStartParameters;
    }

    /**
     * <p>Getter for the field <code>frameworkResourceLoader</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.utils.ResourceLoader} object.
     */
    public ResourceLoader getFrameworkResourceLoader() {
        return frameworkResourceLoader;
    }

    /**
     * <p>Setter for the field <code>rootLogger</code>.</p>
     *
     * @param rootLogger a {@link org.apache.log4j.Logger} object.
     */
    public void setRootLogger(Logger rootLogger) {
        this.rootLogger = rootLogger;
    }

    /**
     * <p>Setter for the field <code>frameworkStartParameters</code>.</p>
     *
     * @param frameworkStartParameters an array of {@link java.lang.String} objects.
     */
    public void setFrameworkStartParameters(String[] frameworkStartParameters) {
        this.frameworkStartParameters = frameworkStartParameters;
    }

    /**
     * <p>Setter for the field <code>frameworkResourceLoader</code>.</p>
     *
     * @param frameworkResourceLoader a {@link net.vortexdata.tsqpf.utils.ResourceLoader} object.
     */
    public void setFrameworkResourceLoader(ResourceLoader frameworkResourceLoader) {
        this.frameworkResourceLoader = frameworkResourceLoader;
    }

    /**
     * <p>Getter for the field <code>frameworkStatusReporter</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.modules.statusreporter.StatusReporter} object.
     */
    public StatusReporter getFrameworkStatusReporter() {
        return frameworkStatusReporter;
    }

    /**
     * <p>Getter for the field <code>frameworkUuidManager</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.modules.uuid.UuidManager} object.
     */
    public UuidManager getFrameworkUuidManager() {
        return frameworkUuidManager;
    }

    /**
     * <p>Getter for the field <code>globalEventHandler</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.listeners.GlobalEventHandler} object.
     */
    public GlobalEventHandler getGlobalEventHandler() {
        return globalEventHandler;
    }

    /**
     * <p>Getter for the field <code>chatCommandListener</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.listeners.ChatCommandListener} object.
     */
    public ChatCommandListener getChatCommandListener() {
        return chatCommandListener;
    }

    /**
     * <p>Getter for the field <code>pluginManager</code>.</p>
     *
     * @return a {@link net.vortexdata.tsqpf.plugins.PluginManager} object.
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
