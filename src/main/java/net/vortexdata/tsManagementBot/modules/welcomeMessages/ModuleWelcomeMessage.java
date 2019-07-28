package net.vortexdata.tsManagementBot.modules.welcomeMessages;

import com.github.theholywaffle.teamspeak3.*;
import net.vortexdata.tsManagementBot.*;

public class ModuleWelcomeMessage implements Runnable {

    private TS3Api _api;

    public ModuleWelcomeMessage(Main main) {
        _api = main.getApi();
    }

    public void run() {

    }
}
