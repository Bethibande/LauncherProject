package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.bootstrap.IService;

import java.awt.*;
import java.util.List;

public interface IWindowManager {

    // get all active/existing window handles
    List<IWindowHandle> getWindows();
    // register a new window with the window name, fps cap and the service/module which owns the window
    IWindowHandle registerWindow(IWindow window, String name, int fps, IService service);
    IWindowHandle registerWindow(IWindow window, String name, int fps, boolean undecorated, IService service);
    IWindowHandle registerWindow(IWindow window, String name, int fps, Shape shape, IService service);
    // destroy a window/windowhandle
    void destroy(IWindowHandle handle);
    // get window by name (name != title)
    IWindowHandle getWindowByName(String name);

}
