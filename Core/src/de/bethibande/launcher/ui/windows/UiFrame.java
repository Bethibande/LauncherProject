package de.bethibande.launcher.ui.windows;

import de.bethibande.launcher.ui.UiUnitConverter;
import de.bethibande.launcher.ui.components.UiComponent;
import de.bethibande.launcher.utils.Vector2f;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class UiFrame implements IUiFrame {

    private final JFrame frame;
    private final IUiRenderConfig renderConfig;
    private final UiRenderer renderer;

    @Getter
    private UiComponent rootComponent;

    public UiFrame() {
        this.frame = new JFrame();
        this.renderConfig = new UiRenderConfig();
        this.renderer = new UiRenderer(this);
    }

    @Override
    public void setRootComponent(UiComponent root) {
        this.rootComponent = root;
        this.rootComponent.setWidth(100);
        this.rootComponent.setHeight(56.25f);
    }

    @Override
    public void show() {
        this.frame.setVisible(true);
    }

    @Override
    public void hide() {
        this.frame.setVisible(false);
    }

    @Override
    public void setSize(float sizeX, float sizeY) {
        this.frame.setSize(UiUnitConverter.sizeUnitToPixelsX(sizeX), UiUnitConverter.sizeUnitToPixelsY(sizeY));
        if(this.rootComponent == null) return;
        this.rootComponent.setWidth(sizeX);
        this.rootComponent.setHeight(sizeY);
    }

    @Override
    public Vector2f getSize() {
        return UiUnitConverter.convertPixelSize(this.frame.getWidth(), this.frame.getHeight());
    }

    @Override
    public void setTitle(String title) {
        this.frame.setTitle(title);
    }

    @Override
    public String getTitle() {
        return this.frame.getTitle();
    }

    @Override
    public IUiRenderConfig getRenderConfig() {
        return this.renderConfig;
    }

    @Override
    public void setResizable(boolean b) {
        this.frame.setResizable(b);
    }

    @Override
    public boolean isResizable() {
        return this.frame.isResizable();
    }

    @Override
    public void pushInForeground() {
        this.frame.toFront();
        this.frame.requestFocus();
    }

    @Override
    public void setIcon(Image image) {
        this.frame.setIconImage(image);
    }

    @Override
    public boolean isFocused() {
        return this.frame.isFocused();
    }

    @Override
    public void setAlwaysOnTop(boolean b) {
        this.frame.setAlwaysOnTop(true);
    }

    @Override
    public boolean isAlwaysOnTop() {
        return this.frame.isAlwaysOnTop();
    }
}
