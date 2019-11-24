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
import com.github.theholywaffle.teamspeak3.api.event.*;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.plugins.PluginContainer;
import net.vortexdata.tsqpf.plugins.PluginManager;

/**
 * Listens for all global Teamspeak events.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class GlobalEventHandler implements TS3Listener {

    private TS3Api ts3Api;
    private Framework framework;

    public GlobalEventHandler(Framework framework) {
        this.framework = framework;
        ts3Api = framework.getApi();
    }

    public void onTextMessage(TextMessageEvent textMessageEvent) {
        if (textMessageEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onTextMessage(textMessageEvent);
        }
        framework.getChatCommandListener().newMessage(textMessageEvent);
    }

    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        if (clientJoinEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onClientJoin(clientJoinEvent);
        }
    }

    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
        if (clientLeaveEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onClientLeave(clientLeaveEvent);
        }
    }

    public void onServerEdit(ServerEditedEvent serverEditedEvent) {
        if (serverEditedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onServerEdit(serverEditedEvent);
        }
    }

    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
        if (channelEditedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelEdit(channelEditedEvent);
        }
    }

    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
        if (channelDescriptionEditedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelDescriptionChanged(channelDescriptionEditedEvent);
        }
    }

    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
        if (clientMovedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onClientMoved(clientMovedEvent);
        }
    }

    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
        if (channelCreateEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelCreate(channelCreateEvent);
        }
    }

    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
        if (channelDeletedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelDeleted(channelDeletedEvent);
        }
    }

    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
        if (channelMovedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelMoved(channelMovedEvent);
        }
    }

    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
        if (channelPasswordChangedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelPasswordChanged(channelPasswordChangedEvent);
        }
    }

    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
        if (privilegeKeyUsedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onPrivilegeKeyUsed(privilegeKeyUsedEvent);
        }
    }
}
