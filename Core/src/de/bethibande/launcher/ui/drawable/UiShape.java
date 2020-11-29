package de.bethibande.launcher.ui.drawable;

import de.bethibande.launcher.ui.UiUnitConverter;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

public class UiShape extends UiDrawable {

    @Getter
    @Setter
    // shapes: rounded_rectangle, rectangle, arc
    private String shape = "rectangle";
    @Getter
    @Setter
    private float cornerSize;
    @Getter
    @Setter
    private float startingAngle;
    @Getter
    @Setter
    private float extendAngle;

    @Override
    public void draw(Graphics2D g) {
        switch (this.shape) {
            case "rounded_rectangle":
                g.setClip(new RoundRectangle2D.Double(0D, 0D, g.getClipBounds().getWidth(), g.getClipBounds().getHeight(), UiUnitConverter.sizeUnitToPixelsX(this.cornerSize), UiUnitConverter.sizeUnitToPixelsY(this.cornerSize)));
                break;
            case "rectangle":
                g.setClip(new Rectangle2D.Double(0, 0, g.getClipBounds().getWidth(), g.getClipBounds().getHeight()));
                break;
            case "arc":
                g.setClip(new Arc2D.Double(0, 0, g.getClipBounds().getWidth(), g.getClipBounds().getHeight(), startingAngle, extendAngle, Arc2D.PIE));
        }
    }
}
