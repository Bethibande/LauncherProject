package de.bethibande.launcher.modules;

import de.bethibande.launcher.events.Event;
import de.bethibande.launcher.events.HandlerList;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ModulesLoadedEvent implements Event {

    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

}
