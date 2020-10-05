package de.bethibande.launcher.events;

public class TestEvent implements Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
