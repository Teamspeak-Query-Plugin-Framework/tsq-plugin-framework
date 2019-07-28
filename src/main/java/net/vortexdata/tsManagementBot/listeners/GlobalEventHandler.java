package net.vortexdata.tsManagementBot.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import net.vortexdata.tsManagementBot.Bot;
import net.vortexdata.tsManagementBot.modules.PluginInterface;
import net.vortexdata.tsManagementBot.modules.PluginManager;

public class GlobalEventHandler implements TS3Listener {

    private TS3Api _api;

    public GlobalEventHandler(Bot bot) {
        _api = bot.getApi();
    }

    public void onTextMessage(TextMessageEvent textMessageEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onTextMessage(textMessageEvent);
        }
    }

    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onClientJoin(clientJoinEvent);
        }
    }

    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onClientLeave(clientLeaveEvent);
        }
    }

    public void onServerEdit(ServerEditedEvent serverEditedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onServerEdit(serverEditedEvent);
        }
    }

    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onChannelEdit(channelEditedEvent);
        }
    }

    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onChannelDescriptionChanged(channelDescriptionEditedEvent);
        }
    }

    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onClientMoved(clientMovedEvent);
        }
    }

    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onChannelCreate(channelCreateEvent);
        }
    }

    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onChannelDeleted(channelDeletedEvent);
        }
    }

    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onChannelMoved(channelMovedEvent);
        }
    }

    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onChannelPasswordChanged(channelPasswordChangedEvent);
        }
    }

    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
        for (PluginInterface pl : PluginManager.getLoadedplugins()) {
            pl.getEventHandler().onPrivilegeKeyUsed(privilegeKeyUsedEvent);
        }
    }
}
