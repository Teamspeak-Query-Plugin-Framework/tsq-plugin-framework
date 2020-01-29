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

import com.github.theholywaffle.teamspeak3.api.event.*;
import net.vortexdata.tsqpf.framework.*;
import net.vortexdata.tsqpf.plugins.PluginContainer;
import net.vortexdata.tsqpf.plugins.PluginManager;

/**
 * Listens for all global Teamspeak events.
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 * @version $Id: $Id
 */
public class GlobalEventHandler implements TS3Listener {

    private FrameworkContainer frameworkContainer;

    /**
     * <p>Constructor for GlobalEventHandler.</p>
     *
     * @param frameworkContainer a {@link net.vortexdata.tsqpf.framework.FrameworkContainer} object.
     */
    public GlobalEventHandler(FrameworkContainer frameworkContainer) {
        this.frameworkContainer = frameworkContainer;

    }

    /**
     * {@inheritDoc}
     *
     * Fires when the framework receives a new text message.
     */
    public void onTextMessage(TextMessageEvent textMessageEvent) {
        if (textMessageEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onTextMessage(textMessageEvent);
        }
        frameworkContainer.getFrameworkChatCommandListener().newMessage(textMessageEvent);
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a client connects to the Teamspeak server.
     */
    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        if (clientJoinEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onClientJoin(clientJoinEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a client disconnects from the Teamspeak server.
     */
    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
        if (clientLeaveEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onClientLeave(clientLeaveEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when the Teamspeak server is edited.
     */
    public void onServerEdit(ServerEditedEvent serverEditedEvent) {
        if (serverEditedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onServerEdit(serverEditedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a channel of the Teamspeak server is edited.
     */
    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
        if (channelEditedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelEdit(channelEditedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when the channel description of any channel on the Teamspeak server is changed.
     */
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
        if (channelDescriptionEditedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelDescriptionChanged(channelDescriptionEditedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a client is moved on the Teamspeak server.
     */
    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
        if (clientMovedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onClientMoved(clientMovedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a new channel is created on the Teamspeak server.
     */
    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
        if (channelCreateEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelCreate(channelCreateEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a channel is deleted on the Teamspeak server.
     */
    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
        if (channelDeletedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelDeleted(channelDeletedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when a channel is moved on the Teamspeak server.
     */
    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
        if (channelMovedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelMoved(channelMovedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when the password of any channel is changed.
     */
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
        if (channelPasswordChangedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onChannelPasswordChanged(channelPasswordChangedEvent);
        }
    }

    /**
     * {@inheritDoc}
     *
     * Fires when any privilege key is used.
     */
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
        if (privilegeKeyUsedEvent.getInvokerId() == frameworkContainer.getTs3Api().whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getTeamspeakPlugin().onPrivilegeKeyUsed(privilegeKeyUsedEvent);
        }
    }
}
