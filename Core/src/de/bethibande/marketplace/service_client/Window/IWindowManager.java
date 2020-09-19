package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.bootstrap.IService;

import java.util.List;

public interface IWindowManager {

    // get all active/existing window handles
    List<IWindowHandle> getWindows();

    // register a new window with the window name, fps cap and the service/module which owns the window
    IWindowHandle registerWindow(IWindow window, String name, int fps, IService service);

    // destroy a window/windowhandle
    void destroy(IWindowHandle handle);

}
