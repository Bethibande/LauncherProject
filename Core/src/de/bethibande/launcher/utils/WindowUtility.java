package de.bethibande.launcher.utils;

import de.bethibande.launcher.service_client.Window.Window;

import java.awt.*;

public class WindowUtility {

    private static java.awt.Dimension sSize = Toolkit.getDefaultToolkit().getScreenSize();;

    // set rendering hints for graphics context and clear it
    public static void setRenderingHintsAndClear(Graphics2D g2d, Color clearColor) {
        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(qualityHints);

        g2d.setColor(clearColor);
        g2d.fillRect(0, 0, g2d.getClipBounds().width, g2d.getClipBounds().height);
    }

    public static void setRenderingHints(Graphics2D g2d) {
        RenderingHints qualityHints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        qualityHints.put(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHints(qualityHints);
    }

    public static int getWindowPercX(Window w, float perc) {
        Dimension size = w.getSize();
        int pixelSizeX = getScreenPercX(size.getWidth());
        return (int)((float)pixelSizeX*(perc/100f));
    }

    public static int getWindowPercY(Window w, float perc) {
        Dimension size = w.getSize();
        int pixelSizeY = getScreenPercY(size.getHeight());
        return (int)((float)pixelSizeY*(perc/100f));
    }

    public static int getScreenPercX(float perc) {
        return (int)(sSize.getWidth()*perc);
    }

    public static int getScreenPercY(float perc) {
        return (int)(sSize.getHeight()*perc);
    }
    
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
