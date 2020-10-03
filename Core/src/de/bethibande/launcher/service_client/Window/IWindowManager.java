package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.bootstrap.IService;

import java.util.List;

public interface IWindowManager {

    // get all active/existing window handles
    List<IWindowHandle> getWindows();

    // register a new window with the window name, fps cap and the service/module which owns the window
    IWindowHandle registerWindow(IWindow window, String name, int fps, IService service);

    // destroy a window/windowhandle
    void destroy(IWindowHandle handle);

}
