package de.bethibande.launcher.networking.events;

import de.bethibande.launcher.events.Event;
import de.bethibande.launcher.events.HandlerList;
import de.bethibande.launcher.networking.connector.ISimpleServerConnector;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConnectorBufferReceivedEvent implements Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() { return handlers; }

    @Getter
    private ISimpleServerConnector connector;
    @Getter
    private byte[] buffer;

    public ConnectorBufferReceivedEvent(byte[] buffer, ISimpleServerConnector connector) {
        this.buffer = buffer;
        this.connector = connector;
    }
}
