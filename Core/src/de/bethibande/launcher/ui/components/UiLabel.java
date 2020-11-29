package de.bethibande.launcher.ui.components;

import de.bethibande.launcher.ui.UiUnitConverter;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class UiLabel extends UiComponent {

    public static final int ALIGN_LEFT = 1;
    public static final int ALIGN_RIGHT = 2;

    public static final int ALIGN_CENTER = 3;

    public static final int ALIGN_TOP = 4;
    public static final int ALIGN_BOTTOM = 5;

    @Getter
    @Setter
    private String text;
    @Getter
    @Setter
    private int horizontalAlign = ALIGN_LEFT;
    @Getter
    @Setter
    private int verticalAlign = ALIGN_TOP;

    @Override
    public void draw(Graphics2D g) {
        getDrawable().draw(g);
        if(text != null) {
            int x = 0, y = 0;
            Rectangle2D r = g.getFontMetrics().getStringBounds(this.text, g);
            if(this.horizontalAlign == ALIGN_RIGHT) x = (int)(UiUnitConverter.sizeUnitToPixelsX(getWidth())-r.getWidth());
            if(this.horizontalAlign == ALIGN_CENTER) x = (int)(UiUnitConverter.sizeUnitToPixelsX(getWidth())/2-r.getWidth()/2);
            if(this.verticalAlign == ALIGN_BOTTOM) y = (int)(UiUnitConverter.sizeUnitToPixelsY(getHeight())-r.getHeight());
            if(this.verticalAlign == ALIGN_CENTER) y = (int)(UiUnitConverter.sizeUnitToPixelsY(getHeight())/2-r.getHeight()/2);

            g.setColor(getForeground().asColor());

            g.drawString(this.text, x, y);
        }
    }
}
