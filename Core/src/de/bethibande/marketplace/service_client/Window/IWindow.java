package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.utils.Dimension;

// a window container based on the basic java jframe
public interface IWindow {

    // set the jframe visible
    void show();
    // set the jframe invisible
    void hide();
    // set the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    void setSize(float width, float height);
    // get the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    Dimension getSize();
    // set the window title
    void setTitle(String title);
    // get the current window title
    String getTitle();
    // set the root component
    void show(IWindowRootComponent component);
    // get the root component of the window
    IWindowRootComponent getRootComponent();
    // get the window handle
    IWindowHandle getHandle();

}
