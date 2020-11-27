package de.bethibande.launcher.ui;

import lombok.Getter;

import java.awt.*;

public class UiResource {

    @Getter
    private final String id;

    private final Object value;

    public UiResource(String id, Object o) {
        this.id = id;
        this.value = o;
    }

    public Color asColor() { return (Color)this.value; }
    public String asString() { return this.value.toString(); }

    @Override
    public String toString() { return this.value.toString(); }
}
