package de.bethibande.launcher.ui.components;

import de.bethibande.launcher.ui.drawable.UiDrawable;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class UiComponent {

    @Getter
    @Setter
    private boolean visible = true;
    @Getter
    @Setter
    private UiDrawable drawable;
    @Getter
    private final List<UiComponent> children = new ArrayList<>();
    @Getter
    @Setter
    private Color background;
    @Getter
    @Setter
    private Color foreground;

    public void draw(Graphics2D g) {
        drawable.draw(g);
    }

}
