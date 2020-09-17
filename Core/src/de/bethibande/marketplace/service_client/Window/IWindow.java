package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.bootstrap.IService;

// a window container based on the basic java jframe
public interface IWindow {

    // set the jframe visible
    void show();
    // set the jframe invisible
    void hide();
    // set the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    void setSize(float width, float height);
    // add a component to the window
    void add(IWindowComponent component);
    // get the window handle
    IWindowHandle getHandle();

}
