package de.bethibande.launcher.service_client.Window.animations;

public class SimpleAnimator implements IAnimator {

    private boolean stopped = false;

    @Override
    // override later
    public void update(float deltaTime) { }

    @Override
    public void stop() {
        this.stopped = true;
    }

    @Override
    public boolean stopped() {
        return this.stopped;
    }
}
