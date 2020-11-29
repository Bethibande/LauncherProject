package de.bethibande.launcher.ui;

import de.bethibande.launcher.Core;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

@NoArgsConstructor
public class UiResource {

    @Getter
    private String id;

    private Object value;

    @Getter
    @Setter
    private UiManager manager;

    public UiResource(String id, Object o) {
        this.id = id;
        this.value = o;
    }

    public Color asColor() {
        if(this.value instanceof Color) {
            return (Color)this.value;
        } if(this.manager != null && this.value instanceof String) {
            String value = (String)this.value;
            if(value.startsWith("@color/")) {
                return this.manager.getResource(value).asColor();
            } else {
                Core.loggerInstance.logError("Tried to cast a resource of the type: '" + this.value.getClass().getName() + "' to 'java.awt.Color'");
                return Color.white;
            }
        } else {
            Core.loggerInstance.logError("Tried to cast a resource of the type: '" + this.value.getClass().getName() + "' to 'java.awt.Color'");
            return Color.white;
        }
    }
    public ImageIcon asImage() {
        if(this.value instanceof ImageIcon) {
            return (ImageIcon)this.value;
        } else {
            Core.loggerInstance.logError("Tried to cast a resource of the type: '" + this.value.getClass().getName() + "' to 'javax.swing.ImageIcon'");
            return null;
        }
    }
    public String asString() { return this.value.toString(); }

    @Override
    public String toString() { return this.value.toString(); }
}
