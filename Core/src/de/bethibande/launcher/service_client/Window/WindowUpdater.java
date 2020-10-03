package de.bethibande.launcher.service_client.Window;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.utils.TimeUtils;
import lombok.Getter;

public class WindowUpdater extends Thread implements IWindowUpdater {

    @Getter
    private IWindowHandle handle;

    private float deltaTime = 0.0f;

    private boolean closeRequested = false;

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
            while(!this.closeRequested) {
                deltaTime = (TimeUtils.getTimeInNano()-lastFrameInNano)/1000000000.0f;
                long started = TimeUtils.getTimeInMillis();

                // update frame
                handle.getWindow().update();

                long finished = TimeUtils.getTimeInMillis();
                lastFrameInNano = TimeUtils.getTimeInNano();

                Thread.sleep((1000/handle.getFPS())-(finished-started));
                if(!this.closeRequested) this.closeRequested = handle.isCloseRequested();
            }
        } catch(InterruptedException e) {
            Core.loggerInstance.logError("The window: " + handle.getWindow().getName() + ", has been closing with an error");
            e.printStackTrace();
        } finally {
            Core.loggerInstance.logMessage("Closing window: " + handle.getWindow().getName());
            handle.getWindow().close();
        }
        this.interrupt();
    }

    @Override
    public void close() {
        this.closeRequested = true;
    }
}
