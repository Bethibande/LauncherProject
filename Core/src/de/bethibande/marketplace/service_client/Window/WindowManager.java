package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.Core;
import de.bethibande.marketplace.bootstrap.IService;
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
        renderer.init(handle);
        renderer.start();
        window.init(handle, name);
        this.windows.add(handle);

        return handle;
    }

    @Override
    public void destroy(IWindowHandle handle) {
        handle.getWindow().close();
        handle.getRenderer().close();
        this.windows.remove(handle);
        Core.loggerInstance.logMessage("Window destroyed: " + handle.getWindow().getName());
    }
}
