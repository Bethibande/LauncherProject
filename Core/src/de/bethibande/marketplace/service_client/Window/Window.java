package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.utils.Dimension;
import de.bethibande.marketplace.utils.WindowUtility;
import lombok.Getter;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Window implements IWindow {

    private JFrame jframe;
    private Dimension currentWindowSize;

    @Getter
    private IWindowHandle handle;
    @Getter
    private JPanel rootComponent;
    @Getter
    private boolean closeRequested = false;
    @Getter
    private String name;

    public void init(IWindowHandle handle, String name) {
        jframe = new JFrame();
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(0);
        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeRequested = true;
            }
        });
        this.handle = handle;
        this.name = name;
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
    public void show(JPanel component) {
        if(this.rootComponent != null) this.jframe.remove(this.rootComponent);
        this.jframe.add(component);
        this.rootComponent = component;
        if(!(component.getLayout() instanceof GroupLayout)) component.setLayout(new GroupLayout(component));
    }

    @Override
    public void close() {
        this.jframe.dispose();
    }

    @Override
    public void update() {
        this.jframe.repaint();
    }

    @Override
    public void initialize() {
        // override in window class, look at IWindow interface for method description
    }
    @Override
    public void updated() {
        // override in window class, look at IWindow interface for method description
    }

}
