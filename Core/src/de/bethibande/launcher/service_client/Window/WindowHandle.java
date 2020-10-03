package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.bootstrap.IService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class WindowHandle implements IWindowHandle {

    @Getter
    private final IWindow window;
    @Getter
    private final IWindowManager manager;
    @Getter
    private final IService service;
    @Getter
    private final IWindowUpdater renderer;
    @Getter
    @Setter
    private int FPS;

    public boolean isCloseRequested() { return this.window.isCloseRequested(); }

}
