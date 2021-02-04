package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.service_client.Window.animations.IAnimator;
import de.bethibande.launcher.utils.TimeUtils;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class WindowUpdater extends Thread implements IWindowUpdater {

    @Getter
    private IWindowHandle handle;

    private float deltaTime = 0.0f;

    private boolean closeRequested = false;

    private final List<IAnimator> animators = new ArrayList<>();

    @Override
    public void init(IWindowHandle handle) {
        this.handle = handle;
    }

    @Override
    public float getDeltaTime() {
        return deltaTime;
    }

    @Override
    public void run() {
        try {
            long lastFrameInNano = TimeUtils.getTimeInNano();
            List<IAnimator> stoppedAnimators = new ArrayList<>();
            while(!this.closeRequested) {
                deltaTime = (TimeUtils.getTimeInNano()-lastFrameInNano)/1000000000.0f;
                long started = TimeUtils.getTimeInMillis();

                // update frame
                handle.getWindow().update();

                long finished = TimeUtils.getTimeInMillis();
                lastFrameInNano = TimeUtils.getTimeInNano();

                for(IAnimator animator : this.animators) {
                    if(animator.stopped()) {
                        stoppedAnimators.add(animator);
                    } else animator.update(getDeltaTime());
                }

                stoppedAnimators.forEach(this.animators::remove);
                stoppedAnimators.clear();

                long timeout = (1000/handle.getFPS())-(finished-started);
                if(timeout <= 0) timeout = 1000/ handle.getFPS();
                Thread.sleep(timeout);
                if(!this.closeRequested) this.closeRequested = handle.isCloseRequested();
            }
        } catch(InterruptedException e) {
            Core.loggerInstance.logError("The window: " + handle.getWindow().getName() + ", has been closing with an error");
            e.printStackTrace();
        } finally {
            Core.loggerInstance.logMessage("Closing window: " + handle.getWindow().getName());
            handle.getWindow().close();
        }
        if(handle.getWindow().isShutdownOnClose()) {
            Core.shutdown(handle.getWindow().getShutdownCode());
        }
        this.interrupt();
    }

    @Override
    public void startAnimator(IAnimator animator) {
        this.animators.add(animator);
        animator.init(this);
    }

    @Override
    public void close() {
        this.closeRequested = true;
    }
}
