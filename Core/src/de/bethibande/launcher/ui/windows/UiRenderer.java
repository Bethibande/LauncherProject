package de.bethibande.launcher.ui.windows;

public class UiRenderer extends Thread implements IUiRenderer {

    private final UiFrame frame;

    public UiRenderer(UiFrame frame) {
        this.frame = frame;
    }

}
