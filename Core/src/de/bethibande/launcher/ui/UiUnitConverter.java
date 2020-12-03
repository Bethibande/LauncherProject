package de.bethibande.launcher.ui;

import de.bethibande.launcher.utils.Vector2f;
import de.bethibande.launcher.utils.WindowUtility;

public class UiUnitConverter {

    public static int sizeUnitToPixelsX(float f) {
        return (int)(WindowUtility.sSize.getWidth()*(f/100f));
    }

    public static int sizeUnitToPixelsY(float f) {
        return (int)(WindowUtility.sSize.getWidth()*(f/100f));
    }

    public static Vector2f convertPixelSize(int width, int height) {
        float x = (width/(float)WindowUtility.sSize.getWidth())*100;
        float y = (height/(float)WindowUtility.sSize.getHeight())*100;
        return new Vector2f(x, y);
    }

}
