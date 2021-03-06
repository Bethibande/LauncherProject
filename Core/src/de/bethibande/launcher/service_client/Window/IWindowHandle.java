package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.bootstrap.IService;

public interface IWindowHandle {

    // get the IWindow instance this handle belongs to
    IWindow getWindow();
    // get the global IWindowManager instance
    IWindowManager getManager();
    // get the service which created the IWindow instance
    IService getService();
    // get the renderer which is rendering the IWindow of this handle
    IWindowUpdater getRenderer();
    // set the window fps cap of the IWindow instance
    void setFPS(int fps);
    // get the current fps cap of the IWindow instance
    int getFPS();
    // is window close requested by user
    boolean isCloseRequested();

}
