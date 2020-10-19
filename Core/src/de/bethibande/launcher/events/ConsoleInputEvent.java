package de.bethibande.launcher.events;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConsoleInputEvent implements Event {

    private static final HandlerList handlers = new HandlerList();

    public HandlerList getHandlers() { return handlers; }

    @Getter
    private String fullCommand;
    @Getter
    private String[] args;

    public ConsoleInputEvent(String fullCommand, String[] args) {
        this.fullCommand = fullCommand;
        this.args = args;
    }

}
