package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.bootstrap.IService;
import de.bethibande.launcher.utils.Dimension;
import de.bethibande.launcher.utils.Vector2f;
import de.bethibande.launcher.utils.WindowUtility;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// register window before setting the size/title and so on
public class Window implements IWindow {

    private JFrame jframe;

    @Getter
    private IWindowHandle handle;
    @Getter
    private JPanel rootComponent;
    @Getter
    private boolean closeRequested = false;
    @Getter
    private String name;
    @Getter
    @Setter
    private boolean shutdownOnClose;
    @Getter
    @Setter
    private int shutdownCode = IService.EXIT_REQUESTED_BY_USER;

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
        jframe.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updated();
            }
        });
        this.handle = handle;
        this.name = name;
    }

    public void init(IWindowHandle handle, String name, boolean undecorated) {
        jframe = new JFrame();
        jframe.setUndecorated(undecorated);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(0);
        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeRequested = true;
            }
        });
        jframe.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updated();
            }
        });
        this.handle = handle;
        this.name = name;
    }
    public void init(IWindowHandle handle, String name, Shape shape) {
        jframe = new JFrame();
        jframe.setUndecorated(true);
        jframe.setShape(shape);
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(0);
        jframe.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeRequested = true;
            }
        });
        jframe.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updated();
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
    }

    @Override
    public Dimension getSize() {
        java.awt.Dimension size = this.jframe.getSize();
        return new Dimension(WindowUtility.getPixelsInScreenPercentX((int)size.getWidth()), WindowUtility.getPixelsInScreenPercentY((int)size.getHeight()));
    }

    @Override
    public void setPosition(float x, float y) {
        this.jframe.setLocation(WindowUtility.getScreenPercentX(x), WindowUtility.getScreenPercentY(y));
    }

    @Override
    public Vector2f getPosition() {
        Point p = this.jframe.getLocation();
        return new Vector2f(WindowUtility.getPixelsInScreenPercentX((int)p.getX()), WindowUtility.getPixelsInScreenPercentY((int)p.getY()));
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
