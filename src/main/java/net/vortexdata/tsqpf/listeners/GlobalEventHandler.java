package net.vortexdata.tsqpf.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.modules.PluginContainer;
import net.vortexdata.tsqpf.modules.PluginManager;

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
        if(textMessageEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onTextMessage(textMessageEvent);
        }
        framework.getChatCommandListener().newMessage(textMessageEvent);
    }

    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        if(clientJoinEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onClientJoin(clientJoinEvent);
        }
    }

    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
        if(clientLeaveEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onClientLeave(clientLeaveEvent);
        }
    }

    public void onServerEdit(ServerEditedEvent serverEditedEvent) {
        if(serverEditedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onServerEdit(serverEditedEvent);
        }
    }

    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
        if(channelEditedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onChannelEdit(channelEditedEvent);
        }
    }

    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
        if(channelDescriptionEditedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onChannelDescriptionChanged(channelDescriptionEditedEvent);
        }
    }

    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
        if(clientMovedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onClientMoved(clientMovedEvent);
        }
    }

    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
        if(channelCreateEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onChannelCreate(channelCreateEvent);
        }
    }

    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
        if(channelDeletedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onChannelDeleted(channelDeletedEvent);
        }
    }

    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
        if(channelMovedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onChannelMoved(channelMovedEvent);
        }
    }

    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
        if(channelPasswordChangedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onChannelPasswordChanged(channelPasswordChangedEvent);
        }
    }

    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
        if(privilegeKeyUsedEvent.getInvokerId() == ts3Api.whoAmI().getId()) return;
        for (PluginContainer pl : PluginManager.getLoadedPlugins()) {
            pl.getPluginInterface().onPrivilegeKeyUsed(privilegeKeyUsedEvent);
        }
    }
}
