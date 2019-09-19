package net.vortexdata.tsqpf.listeners;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

public interface ChatCommandInterface {

    public void gotCalled(TextMessageEvent event);
}
