package de.bethibande.marketplace.service_client.Window;

import java.util.List;

public interface IWindowManager {

    // get all active/existing window handles
    List<IWindowHandle> getWindows();

    // create a new window
    IWindowHandle createWindow(String title);
    // width and height in percent (based on the screen size, ranging from 100.0f - 0.0f)
    IWindowHandle createWindow(String title, float width, float height);
    // fps -> fps cap for the window
    IWindowHandle createWindow(String title, int fps);
    IWindowHandle createWindow(String title, float width, float height, int fps);

    // destroy a window/windowhandle
    void destroy(IWindowHandle handle);

}
