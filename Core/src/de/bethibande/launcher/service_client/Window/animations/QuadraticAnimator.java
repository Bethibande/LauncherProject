package de.bethibande.launcher.service_client.Window.animations;

// TODO: implement later
public class QuadraticAnimator extends SimpleAnimator {

    private final float start;

    private final float scale;

    private final float xShift;

    private final float yShift;

    private final float xIncreasePerSecond;

    private final int time;

    private final IAnimationUpdate update;

    private float x;

    private int timeLeft;

    public QuadraticAnimator(float start, float scale, float xShift, float yShift, float xIncreasePerSecond, int time, IAnimationUpdate update) {
        this.start = start;
        this.scale = scale;
        this.xShift = xShift;
        this.yShift = yShift;
        this.xIncreasePerSecond = xIncreasePerSecond;
        this.time = time;
        this.update = update;

        this.x = start;
        this.timeLeft = time;
    }

    @Override
    public void update(float deltaTime) {
        float step = xIncreasePerSecond*deltaTime;
        this.x += step;
    }
}
