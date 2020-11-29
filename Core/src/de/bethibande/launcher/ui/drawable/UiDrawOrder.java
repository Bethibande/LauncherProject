package de.bethibande.launcher.ui.drawable;

import lombok.Getter;

import java.awt.*;
import java.util.LinkedList;

public class UiDrawOrder extends UiDrawable {

    @Getter
    private final LinkedList<UiDrawable> children = new LinkedList<>();

    public void addChild(UiDrawable drawable) {
        this.children.add(drawable);
    }

    public void removeChild(UiDrawable drawable) {
        this.children.remove(drawable);
    }

    @Override
    public void draw(Graphics2D g) {
        for(UiDrawable drawable : this.children) {
            drawable.draw(g);
        }
    }
}
