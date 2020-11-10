package de.bethibande.launcher.service_client.Window.animations;

import de.bethibande.launcher.service_client.Window.IWindowUpdater;

public interface IAnimator {

    // internal method
    void init(IWindowUpdater updater);

    // called each frame
    void update(float deltaTime);

    // stop the animator
    void stop();
    // checks if the animator has been stopped using stop();
    boolean stopped();

    IAnimator finish(Runnable r);

    IAnimator ultimately(Runnable r);


}
