package net.vortexdata.tsManagementBot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import net.vortexdata.tsManagementBot.configs.*;
import net.vortexdata.tsManagementBot.listeners.GlobalEventHandler;

public class Main {

    private static Main _instace;
    private TS3Api _api;


    public static void main(String[] args) {
        _instace = new Main();
        _instace.init();
    }

    private void init() {

        ConfigMain configMain = new ConfigMain();
        configMain.load();

        final TS3Config config = new TS3Config();
        config.setHost(configMain.getProperty("serverAddress"));
        final TS3Query query = new TS3Query(config);
        query.connect();
        _api = query.getApi();
        _api.login(configMain.getProperty("queryUser"), configMain.getProperty("queryPassword"));
        _api.selectVirtualServerById(1);
        _api.setNickname(configMain.getProperty("botNickname"));

        _api.sendServerMessage("Online");
        _api.registerAllEvents();
        _api.addTS3Listeners(new GlobalEventHandler(this));
    }


    public TS3Api getApi() {
        return _api;
    }
}
