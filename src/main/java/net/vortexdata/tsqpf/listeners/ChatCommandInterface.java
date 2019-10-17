package net.vortexdata.tsqpf.listeners;

import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

/**
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public interface ChatCommandInterface {

    void gotCalled(TextMessageEvent event);
}
