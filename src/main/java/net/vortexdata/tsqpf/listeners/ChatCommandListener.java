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

package net.vortexdata.tsqpf.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import net.vortexdata.tsqpf.*;
import net.vortexdata.tsqpf.configs.ConfigMessages;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Trigger and manager for Teamspeak chat commands.
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 2.0.0
 */
public class ChatCommandListener {

    private String messageCommandNotFound;
    private HashMap<String, ArrayList<ChatCommandInterface>> commandList = new HashMap<>();
    private FrameworkContainer frameworkContainer;

    public ChatCommandListener(FrameworkContainer frameworkContainer) {
        this.messageCommandNotFound = frameworkContainer.getConfig(new ConfigMessages().getPath()).getProperty("chatCommandUnknown");
        this.frameworkContainer = frameworkContainer;
    }

    /**
     * Activated when the Framework receives a new private message.
     *
     * @param msg The events object
     */
    public void newMessage(TextMessageEvent msg) {
        TS3Api ts3Api = frameworkContainer.getTs3Api();
        // Send TSQPF Info
        if (msg.getMessage().startsWith("!info")) {
            ts3Api.sendPrivateMessage(msg.getInvokerId(), "This server is running the VortexdataNET Teamspeak Query Plugin Framework");
            ts3Api.sendPrivateMessage(msg.getInvokerId(), "More info: https://projects.vortexdata.net/tsq-plugin-framework");
            return;
        }


        if (commandList.size() == 0) {
            ts3Api.sendPrivateMessage(msg.getInvokerId(), messageCommandNotFound);
            return;
        }

        boolean commandFound = false;
        for (String prefix : commandList.keySet()) {
            if (msg.getMessage().startsWith(prefix)) {
                commandFound = true;
                for (ChatCommandInterface cmd : commandList.get(prefix)) {
                    cmd.gotCalled(msg);
                }
            }
        }

        if (!commandFound)
            ts3Api.sendPrivateMessage(msg.getInvokerId(), messageCommandNotFound);
    }

    /**
     * Registers and enables a new command.
     *
     * @param cmd    Instance of command class
     * @param prefix How the command is referenced (eg !help)
     */
    public void registerNewCommand(ChatCommandInterface cmd, String prefix) {
        ArrayList<ChatCommandInterface> cmds;

        if ((cmds = commandList.get(prefix)) != null) {
            cmds.add(cmd);
        } else {
            cmds = new ArrayList<>();
            commandList.put(prefix, cmds);
            cmds.add(cmd);
        }


    }
}
