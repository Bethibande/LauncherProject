package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.utils.Dimension;
import de.bethibande.launcher.utils.Vector2f;

import javax.swing.*;
import java.awt.*;

// a window container based on the basic java jframe
// register window before setting the size/title and so on
public interface IWindow {

    // init window instance, hand over handle and name
    void init(IWindowHandle handle, String name);
    void init(IWindowHandle handle, String name, boolean undecorated);
    void init(IWindowHandle handle, String name, Shape shape);
    // set the jframe visible
    void show();
    // set the jframe invisible
    void hide();
    // set the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    void setSize(float width, float height);
    // get the window size in percent (based on the screen size, ranging from 100.0f - 0.0f)
    Dimension getSize();
    // set window position on screen (based on the screen size, ranging from 100.0f - 0.0f)
    void setPosition(float x, float y);
    // get window position on screen (based on the screen size, ranging from 100.0f - 0.0f)
    Vector2f getPosition();
    // set the window title
    void setTitle(String title);
    // get the current window title
    String getTitle();
    // set the window icon
    void setIcon(Image image);
    // set the root component
    void show(JPanel root);
    // check if the window is visible
    boolean isVisible();
    // push window to the foreground and request focus
    void push();
    // set window resizable
    void setResizable(boolean b);
    // get whether the window is resizable or not
    boolean isResizable();
    // get the root component of the window
    JPanel getRootComponent();
    // get the window handle
    IWindowHandle getHandle();
    // is close requested by user
    boolean isCloseRequested();
    // close the window
    void close();
    // update the jframe
    void update();
    // get the window name
    String getName();

    // if true, the entire application will stop when this window has been closed
    // !!! Only works if the user closes the application using the close button provided by the os
    void setShutdownOnClose(boolean b);
    // set the exit code, only used if setShutdownOnClose is true
    void setShutdownCode(int exitCode);

    boolean isShutdownOnClose();
    int getShutdownCode();

    // called when the window/jframe has been registered at the IWindowManager
    void initialize();
    // called when the window/jframe size was changed to update the size of the jcomponents of the jframe
    void updated();

}
