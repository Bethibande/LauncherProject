package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.bootstrap.IService;
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
    private final IWindowRenderer renderer;
    @Getter
    @Setter
    private int FPS;

}
