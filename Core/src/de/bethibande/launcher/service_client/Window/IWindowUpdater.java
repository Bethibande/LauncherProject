package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.service_client.Window.animations.IAnimator;

public interface IWindowUpdater {

    // give the renderer the IWindowHandle of the IWindow it will be rendering
    void init(IWindowHandle handle);
    // get the IWindowHandle assigned to this renderer
    IWindowHandle getHandle();
    // get delta time (used for animations to be independent from fps cap, time elapsed since the last frame was drawn)
    float getDeltaTime();
    // will cause the updater to close/stop/interrupt when it tries to update the window for the next time
    void close();

    void startAnimator(IAnimator animator);

}
