package net.vortexdata.tsManagementBot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.*;
import net.vortexdata.tsManagementBot.configs.*;
import net.vortexdata.tsManagementBot.listeners.GlobalEventHandler;
import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

public class Main {

    private static Main _instance;
    private TS3Api _api;
    private static final Logger logger = LogManager.getRootLogger();


    public static void main(String[] args) {
        _instance = new Main();
        _instance.init();
    }

    private void init() {

        logger.info("Initializing... Please wait.");

        // Load main config
        ConfigMain configMain = new ConfigMain();
        logger.debug("Loading main config...");
        configMain.load();
        logger.debug("Main config loaded.");

        // Create config
        final TS3Config config = new TS3Config();
        logger.debug("Trying to set server address...");
        config.setHost(configMain.getProperty("serverAddress"));
        logger.debug("Server address set.");
        // Create query
        final TS3Query query = new TS3Query(config);
        logger.debug("Trying to connect to server...");
        try {
            query.connect();
        } catch (Exception e) {
            logger.error("Connection to server failed, dumping error information.", e);
            System.exit(0);
        }
        logger.info("Successfully established connection to server.");
        _api = query.getApi();
        try {
            logger.debug("Trying to sign into query...");
            _api.login(configMain.getProperty("queryUser"), configMain.getProperty("queryPassword"));
            logger.info("Successfully signed into query.");
        } catch (TS3ConnectionFailedException e) {
            logger.error("Failed to sign into query, dumping error information.", e);
            System.exit(0);
        }

        // Select virtual host
        logger.debug("Trying to select virtual server...");
        try {
            _api.selectVirtualServerById(Integer.parseInt(configMain.getProperty("virtualServer")));
            logger.info("Successfully selected virtual server.");
        } catch (Exception e) {
            logger.error("Failed to select virtual server, dumping error details.", e);
            System.exit(0);
        }

        try {
            logger.debug("Trying to set nickname...");
            _api.setNickname(configMain.getProperty("botNickname"));
            logger.debug("Successfully set nickname.");
        } catch (Exception e) {
            logger.error("Failed to set nickname, dumping error details.", e);
            System.exit(0);
        }


        _api.sendServerMessage("Online");
        _api.registerAllEvents();
        _api.addTS3Listeners(new GlobalEventHandler(this));
    }


    public TS3Api getApi() {
        return _api;
    }
    public Logger getLogger() {
        return logger;
    }
}
