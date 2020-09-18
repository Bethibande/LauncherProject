package de.bethibande.marketplace.service_client.Window;

import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

// TODO: implement
public class WindowManager implements IWindowManager {

    @Getter
    private final List<IWindowHandle> windows = new ArrayList<>();

    @Override
    public IWindowHandle createWindow(String title) {
        throw new NotImplementedException();
    }

    @Override
    public IWindowHandle createWindow(String title, float width, float height) {
        throw new NotImplementedException();
    }

    @Override
    public IWindowHandle createWindow(String title, int fps) {
        throw new NotImplementedException();
    }

    @Override
    public IWindowHandle createWindow(String title, float width, float height, int fps) {
        throw new NotImplementedException();
    }

    @Override
    public void destroy(IWindowHandle handle) {
        throw new NotImplementedException();
    }
}
