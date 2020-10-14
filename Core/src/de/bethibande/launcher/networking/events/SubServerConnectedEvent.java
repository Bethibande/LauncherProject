package de.bethibande.launcher.networking.events;

import de.bethibande.launcher.events.Event;
import de.bethibande.launcher.events.HandlerList;
import de.bethibande.launcher.networking.server.NetworkSubServer;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SubServerConnectedEvent implements Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Getter
    private NetworkSubServer subServer;

    public SubServerConnectedEvent(NetworkSubServer subServer) {
        this.subServer = subServer;
    }

}
