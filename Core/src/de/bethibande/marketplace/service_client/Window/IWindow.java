package de.bethibande.marketplace.service_client.Window;

import de.bethibande.marketplace.utils.Dimension;

import javax.swing.*;

// a window container based on the basic java jframe
public interface IWindow {

    // init window instance, hand over handle and name
    void init(IWindowHandle handle, String name);
    // set the jframe visible
    void show();
    // set the jframe invisible
    void hide();
    // set the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    void setSize(float width, float height);
    // get the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    Dimension getSize();
    // set the window title
    void setTitle(String title);
    // get the current window title
    String getTitle();
    // set the root component
    void show(JPanel root);
    // get the root component of the window
    JPanel getRootComponent();
    // get the window handle
    IWindowHandle getHandle();
    // is close requested by user
    boolean isCloseRequested();
    // close the window
    void close();
    // updat the jframe
    void update();
    // get the window name
    String getName();

    // called when the window/jframe has been registered at the IWindowManager
    void initialize();
    // called when the window/jframe size was changed to update the size of the jcomponents of the jframe
    void updated();

}
