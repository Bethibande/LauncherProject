package de.bethibande.launcher.ui.windows;

import de.bethibande.launcher.ui.components.UiComponent;
import de.bethibande.launcher.utils.Vector2f;

import java.awt.*;

public interface IUiFrame {

    // set the root component which is gonna be displayed on the frame
    void setRootComponent(UiComponent root);
    // get the root component which is currently being displayed
    UiComponent getRootComponent();

    // make the frame visible
    void show();
    // make the frame invisible
    void hide();

    // set the frame size
    // 100 x * 56.25 = screen size
    void setSize(float sizeX, float sizeY);
    // get the frame size
    Vector2f getSize();

    // set the title of the frame
    void setTitle(String title);
    // get the current title of the frame
    String getTitle();

    // get the rendering config
    IUiRenderConfig getRenderConfig();

    // enabled/disable resizing of the frame
    void setResizable(boolean b);
    // check whether the frame is resizable or not
    boolean isResizable();

    // push the frame to foreground and focus it
    void pushInForeground();

    // set the frame icon
    void setIcon(Image image);
    // check whether the frame is focused or not
    boolean isFocused();

    // set the frame to be always on top
    void setAlwaysOnTop(boolean b);
    // check whether the frame is always on top or not
    boolean isAlwaysOnTop();

}
