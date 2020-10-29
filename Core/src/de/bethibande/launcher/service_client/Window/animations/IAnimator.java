package de.bethibande.launcher.service_client.Window.animations;

public interface IAnimator {

    // called each frame
    void update(float deltaTime);

    // stop the animator
    void stop();
    // checks if the animator has been stopped using stop();
    boolean stopped();


}
