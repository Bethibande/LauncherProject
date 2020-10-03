package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.bootstrap.IService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WindowManager implements IWindowManager {

    @Getter
    private final List<IWindowHandle> windows = new ArrayList<>();

    @Override
    public IWindowHandle registerWindow(IWindow window, String name, int fps, IService service) {
        WindowUpdater renderer = new WindowUpdater();
        WindowHandle handle = new WindowHandle(window, this, service, renderer, fps);
        window.init(handle, name);
        window.setTitle(name);
        renderer.init(handle);
        renderer.start();
        this.windows.add(handle);

        window.initialize();

        return handle;
    }

    @Override
    public void destroy(IWindowHandle handle) {
        handle.getWindow().close();
        handle.getRenderer().close();
        this.windows.remove(handle);
        Core.loggerInstance.logMessage("Window destroyed: " + handle.getWindow().getName());
    }

    @Override
    public IWindowHandle getWindowByName(String name) {
        for(IWindowHandle handle : this.windows) {
            if(handle.getWindow().getName().equalsIgnoreCase(name)) return handle;
        }
        return null;
    }
}
