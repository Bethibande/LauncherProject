package de.bethibande.launcher.utils;

import java.awt.*;

public class WindowUtility {

    // returns how much pixels of the screen width the specified percent amount is
    public static int getScreenPercentX(float percent) {
        return (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*(percent/100f));
    }

    // returns how much pixels of the screen height the specified percent amount is
    public static int getScreenPercentY(float percent) {
        return (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*(percent/100f));
    }

    // returns pixels in percent of screen size (0.0f-100.0f)
    public static float getPixelsInScreenPercentX(int x) {
        return (float)((float)x/Toolkit.getDefaultToolkit().getScreenSize().getWidth());
    }

    // returns pixels in percent of screen size (0.0f-100.0f)
    public static float getPixelsInScreenPercentY(int y) {
        return (float)((float)y/Toolkit.getDefaultToolkit().getScreenSize().getHeight());
    }

}
