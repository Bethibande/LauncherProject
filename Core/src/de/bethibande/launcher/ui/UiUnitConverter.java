package de.bethibande.launcher.ui;

import de.bethibande.launcher.utils.WindowUtility;

public class UiUnitConverter {

    public static int sizeUnitToPixelsX(float f) {
        return (int)(WindowUtility.sSize.getWidth()*(f/100f));
    }

    public static int sizeUnitToPixelsY(float f) {
        return (int)(WindowUtility.sSize.getWidth()*(f/100f));
    }

}
