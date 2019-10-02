package net.vortexdata.tsqpf.modules;

import com.github.theholywaffle.teamspeak3.api.event.*;

/**
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public abstract class EventHandler {

    public void onTextMessage(TextMessageEvent textMessageEvent) {

    }

    public void onClientJoin(ClientJoinEvent clientJoinEvent) {

    }

    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

    }

    public void onServerEdit(ServerEditedEvent serverEditedEvent) {

    }

    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

    }

    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

    }

    public void onClientMoved(ClientMovedEvent clientMovedEvent) {

    }

    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

    }

    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

    }

    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

    }

    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

    }

    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

    }
}
