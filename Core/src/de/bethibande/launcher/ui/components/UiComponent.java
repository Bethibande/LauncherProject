package de.bethibande.launcher.ui.components;

import de.bethibande.launcher.ui.UiResource;
import de.bethibande.launcher.ui.drawable.UiDrawable;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class UiComponent {

    @Getter
    @Setter
    private String id;
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
    private UiResource background;
    @Getter
    @Setter
    private UiResource foreground;

    @Getter
    @Setter
    private float top;
    @Getter
    @Setter
    private float left;
    @Getter
    @Setter
    private float width;
    @Getter
    @Setter
    private float height;

    public UiComponent() {}

    public void draw(Graphics2D g) {
        drawable.draw(g);
    }

    public UiComponent findComponentById(String id) {
        if(this.id.equalsIgnoreCase(id)) return this;

        for(UiComponent child : this.children) {
            if(child.getId() != null) {
                if(child.getId().equalsIgnoreCase(id)) return child;
            } else {
                UiComponent comp = child.findComponentById(id);
                if(comp != null && comp.getId() != null && comp.getId().equalsIgnoreCase(id)) return comp;
            }
        }
        return null;
    }

}
