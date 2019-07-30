package net.vortexdata.tsManagementBot.listeners;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.*;
import net.vortexdata.tsManagementBot.Bot;
import net.vortexdata.tsManagementBot.modules.PluginContainer;
import net.vortexdata.tsManagementBot.modules.PluginManager;

public class GlobalEventHandler implements TS3Listener {

    private TS3Api _api;

    public GlobalEventHandler(Bot bot) {
        _api = bot.getApi();
    }

    public void onTextMessage(TextMessageEvent textMessageEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onTextMessage(textMessageEvent);
        }
    }

    public void onClientJoin(ClientJoinEvent clientJoinEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onClientJoin(clientJoinEvent);
        }
    }

    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onClientLeave(clientLeaveEvent);
        }
    }

    public void onServerEdit(ServerEditedEvent serverEditedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onServerEdit(serverEditedEvent);
        }
    }

    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onChannelEdit(channelEditedEvent);
        }
    }

    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onChannelDescriptionChanged(channelDescriptionEditedEvent);
        }
    }

    public void onClientMoved(ClientMovedEvent clientMovedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onClientMoved(clientMovedEvent);
        }
    }

    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onChannelCreate(channelCreateEvent);
        }
    }

    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onChannelDeleted(channelDeletedEvent);
        }
    }

    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onChannelMoved(channelMovedEvent);
        }
    }

    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onChannelPasswordChanged(channelPasswordChangedEvent);
        }
    }

    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {
        for (PluginContainer pl : PluginManager.getLoadedplugins()) {
            pl.getPluginInterface().onPrivilegeKeyUsed(privilegeKeyUsedEvent);
        }
    }
}
