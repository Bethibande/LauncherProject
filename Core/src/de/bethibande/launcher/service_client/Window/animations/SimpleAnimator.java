package de.bethibande.launcher.service_client.Window.animations;

import de.bethibande.launcher.service_client.Window.IWindowUpdater;
import lombok.Getter;

public class SimpleAnimator implements IAnimator {

    private boolean stopped = false;

    @Getter
    private IWindowUpdater updater;

    @Getter
    private Runnable finish;

    private Runnable ultimately;

    @Override
    public void init(IWindowUpdater updater) {
        this.updater = updater;
    }

    @Override
    // override later
    public void update(float deltaTime) { }

    @Override
    public void stop() {
        if(ultimately != null) ultimately.run();
        this.stopped = true;
    }

    @Override
    public boolean stopped() {
        return this.stopped;
    }

    @Override
    public IAnimator finish(Runnable r) {
        this.finish = r;
        return this;
    }

    @Override
    public IAnimator ultimately(Runnable r) {
        this.ultimately = r;
        return this;
    }
}
