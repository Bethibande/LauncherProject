package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.utils.Dimension;
import de.bethibande.marketplace.utils.WindowUtility;
import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;

public class Window implements IWindow {

    private final JFrame jframe;
    private Dimension currentWindowSize;

    @Getter
    private final IWindowHandle handle;
    @Getter
    private IWindowRootComponent rootComponent;

    public Window(IWindowHandle handle) {
        jframe = new JFrame();
        this.handle = handle;
    }

    @Override
    public void show() {
        this.jframe.setVisible(true);
    }

    @Override
    public void hide() {
        this.jframe.setVisible(false);
    }

    @Override
    public void setSize(float width, float height) {
        this.jframe.setSize(WindowUtility.getScreenPercentX(width), WindowUtility.getScreenPercentY(height));
        this.currentWindowSize = new Dimension(width, height);
    }

    @Override
    public Dimension getSize() {
        return this.currentWindowSize;
    }

    @Override
    public void setTitle(String title) {
        this.jframe.setTitle(title);
    }

    @Override
    public String getTitle() {
        return this.jframe.getTitle();
    }

    @Override
    public void show(IWindowRootComponent component) {
        this.rootComponent = component;
    }
}
