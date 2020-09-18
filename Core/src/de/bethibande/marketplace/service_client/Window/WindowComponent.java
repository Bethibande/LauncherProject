package de.bethibande.marketplace.service_client.Window;

import lombok.Getter;

import java.util.LinkedList;

public class WindowComponent implements IWindowComponent {

    @Getter
    private final LinkedList<IWindowComponent> components = new LinkedList<>();
    @Getter
    private boolean visible;

    @Override
    public void add(IWindowComponent component) {
        this.components.add(component);
    }

    @Override
    public void remove(IWindowComponent component) {
        this.components.remove(component);
    }

    @Override
    public void show() {
        this.visible = true;
    }

    @Override
    public void hide() {
        this.visible = false;
    }
}
