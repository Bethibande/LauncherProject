package de.bethibande.launcher.ui.drawable;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class UiShape extends UiDrawable {

    @Getter
    @Setter
    // shapes: roundedRectangle, oval, arc, rectangle
    private String shape = "rectangle";

    @Override
    public void draw(Graphics2D g) {
        switch (this.shape) {
            case "roundedrectangle":
                g.setClip(new RoundRectangle2D.Double());
        }
    }
}
