package net.vortexdata.tsqpf.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.configs.ConfigMain;

import java.awt.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Set;

public class ChatCommandListener {

    private ConfigMain config;
    private String messageCommandNotFound;
    private TS3Api _api;

    public ChatCommandListener(Framework Framework, ConfigMain config) {
        this.messageCommandNotFound = config.getProperty("messageChatCommandNotFound");
        _api = Framework.getApi();
    }

    public void newMessage(TextMessageEvent msg) {

        if(commandList.size() == 0) return;

        // Send TSQPF Info
        if (msg.getMessage().startsWith("!info")) {
            _api.sendPrivateMessage(msg.getInvokerId(), "This server is running the VortexdataNET Teamspeak Query Plugin Framework");
            _api.sendPrivateMessage(msg.getInvokerId(), "More info: https://projects.vortexdata.net/tsq-plugin-framework");
            return;
        }

        boolean commandFound = false;
        for (String prefix : commandList.keySet()) {
            if(msg.getMessage().startsWith(prefix)) {
                commandFound = true;
                for (ChatCommandInterface cmd : commandList.get(prefix)) {
                    cmd.gotCalled(msg);
                }
            }
        }

        if (!commandFound)
            _api.sendPrivateMessage(msg.getInvokerId(), messageCommandNotFound);
    }

    private HashMap<String, ArrayList<ChatCommandInterface>> commandList = new HashMap<>();
    public void registerNewCommand(ChatCommandInterface cmd, String prefix) {
        ArrayList<ChatCommandInterface> cmds;

        if((cmds = commandList.get(prefix)) != null) {
            cmds.add(cmd);
        }
        else {
            cmds = new ArrayList<>();
            commandList.put(prefix, cmds);
            cmds.add(cmd);
        }


    }
}
