package net.vortexdata.TsManagementBot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import net.vortexdata.TsManagementBot.Listeners.GlobalEventHandler;

public class Main {

    private static Main _instace;
    private TS3Api _api;


    public static void main(String[] args) {
        _instace = new Main();
        _instace.init();
    }

    private void init() {
        final TS3Config config = new TS3Config();
        config.setHost("ts.vortexdata.net");
        final TS3Query query = new TS3Query(config);
        query.connect();
        _api = query.getApi();
        _api.login("serveradmin", "pw");
        _api.selectVirtualServerById(1);
        _api.setNickname("Vortexdata Management Bot");

        _api.registerAllEvents();
        _api.addTS3Listeners(new GlobalEventHandler(this));
    }


    public TS3Api getApi() {
        return _api;
    }
}
