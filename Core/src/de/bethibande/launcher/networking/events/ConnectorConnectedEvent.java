package de.bethibande.launcher.networking.events;

import de.bethibande.launcher.events.Event;
import de.bethibande.launcher.events.HandlerList;
import de.bethibande.launcher.networking.connector.SimpleServerConnector;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConnectorConnectedEvent implements Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Getter
    private SimpleServerConnector connector;

    public ConnectorConnectedEvent(SimpleServerConnector connector) {
        this.connector = connector;
    }

}
