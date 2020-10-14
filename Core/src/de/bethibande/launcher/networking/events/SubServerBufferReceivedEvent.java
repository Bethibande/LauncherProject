package de.bethibande.launcher.networking.events;

import de.bethibande.launcher.events.Event;
import de.bethibande.launcher.events.HandlerList;
import de.bethibande.launcher.networking.server.NetworkSubServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubServerBufferReceivedEvent implements Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Getter
    private byte[] buffer;
    @Getter
    private NetworkSubServer subServer;

    public SubServerBufferReceivedEvent(byte[] buffer, NetworkSubServer server) {
        this.buffer = buffer;
        this.subServer = server;
    }

}
