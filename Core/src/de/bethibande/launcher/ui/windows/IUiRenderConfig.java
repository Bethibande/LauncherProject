package de.bethibande.launcher.ui.windows;

public interface IUiRenderConfig {

    // set the current fps cap
    void setFpsCap(int fps);
    // get the current fps cap
    int getFpsCap();

    // enabled/disable antialiasing
    void setAntialiasingEnabled(boolean b);
    // check whether antialiasing is enabled or not
    boolean isAntialiasingEnabled();

}
