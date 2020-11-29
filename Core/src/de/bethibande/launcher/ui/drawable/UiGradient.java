package de.bethibande.launcher.ui.drawable;

import de.bethibande.launcher.ui.UiResource;
import de.bethibande.launcher.utils.MathUtils;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class UiGradient extends UiDrawable {

    @Getter
    @Setter
    private UiResource startColor;
    @Getter
    @Setter
    private UiResource endColor;
    @Getter
    @Setter
    private float angle = 0;

    @Override
    public void draw(Graphics2D g) {
        int xCenter = (int)g.getClipBounds().getWidth()/2;
        int yCenter = (int)g.getClipBounds().getHeight()/2;
        int[] startColorPos = MathUtils.rotatePoint(0, yCenter, xCenter, yCenter, this.angle);
        int[] endColorPos = MathUtils.rotatePoint((int)g.getClipBounds().getWidth(), yCenter, xCenter, yCenter, this.angle);
        GradientPaint paint = new GradientPaint(startColorPos[0], startColorPos[1], startColor.asColor(), endColorPos[0], endColorPos[1], endColor.asColor());
        g.setPaint(paint);
        g.fillRect(0, 0, (int)g.getClipBounds().getWidth(), (int)g.getClipBounds().getHeight());
    }
}
