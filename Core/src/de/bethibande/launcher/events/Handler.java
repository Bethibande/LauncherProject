package de.bethibande.launcher.events;

import lombok.Getter;

import java.lang.reflect.Method;

public class Handler {

    @Getter
    private final Listener listener;
    @Getter
    private final Method m;

    public Handler(Listener listener, Method m) {
        this.listener = listener;
        this.m = m;
    }

}
