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

package net.vortexdata.tsqpf.modules.eventhandler;

import com.github.theholywaffle.teamspeak3.api.event.*;

/**
 * <p>Abstract EventHandler class.</p>
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 * @version $Id: $Id
 */
public abstract class EventHandler {

    /**
     * <p>onTextMessage.</p>
     *
     * @param textMessageEvent a {@link com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent} object.
     */
    public void onTextMessage(TextMessageEvent textMessageEvent) {

    }

    /**
     * <p>onClientJoin.</p>
     *
     * @param clientJoinEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent} object.
     */
    public void onClientJoin(ClientJoinEvent clientJoinEvent) {

    }

    /**
     * <p>onClientLeave.</p>
     *
     * @param clientLeaveEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent} object.
     */
    public void onClientLeave(ClientLeaveEvent clientLeaveEvent) {

    }

    /**
     * <p>onServerEdit.</p>
     *
     * @param serverEditedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent} object.
     */
    public void onServerEdit(ServerEditedEvent serverEditedEvent) {

    }

    /**
     * <p>onChannelEdit.</p>
     *
     * @param channelEditedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent} object.
     */
    public void onChannelEdit(ChannelEditedEvent channelEditedEvent) {

    }

    /**
     * <p>onChannelDescriptionChanged.</p>
     *
     * @param channelDescriptionEditedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ChannelDescriptionEditedEvent} object.
     */
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent channelDescriptionEditedEvent) {

    }

    /**
     * <p>onClientMoved.</p>
     *
     * @param clientMovedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent} object.
     */
    public void onClientMoved(ClientMovedEvent clientMovedEvent) {

    }

    /**
     * <p>onChannelCreate.</p>
     *
     * @param channelCreateEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ChannelCreateEvent} object.
     */
    public void onChannelCreate(ChannelCreateEvent channelCreateEvent) {

    }

    /**
     * <p>onChannelDeleted.</p>
     *
     * @param channelDeletedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ChannelDeletedEvent} object.
     */
    public void onChannelDeleted(ChannelDeletedEvent channelDeletedEvent) {

    }

    /**
     * <p>onChannelMoved.</p>
     *
     * @param channelMovedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ChannelMovedEvent} object.
     */
    public void onChannelMoved(ChannelMovedEvent channelMovedEvent) {

    }

    /**
     * <p>onChannelPasswordChanged.</p>
     *
     * @param channelPasswordChangedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.ChannelPasswordChangedEvent} object.
     */
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent channelPasswordChangedEvent) {

    }

    /**
     * <p>onPrivilegeKeyUsed.</p>
     *
     * @param privilegeKeyUsedEvent a {@link com.github.theholywaffle.teamspeak3.api.event.PrivilegeKeyUsedEvent} object.
     */
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent privilegeKeyUsedEvent) {

    }
}
