package de.bethibande.launcher.service_client.Window.animations;

public class AToBAnimator extends SimpleAnimator {

    private final float a;
    private final float b;
    private final int time;
    private final IAnimationUpdate update;

    private float f;

    public AToBAnimator(float a, float b, int time, IAnimationUpdate update) {
        this.a = a;
        this.b = b;
        this.time = time;
        this.update = update;
        this.f = a;
    }

    @Override
    public void update(float deltaTime) {
        float deltaTransformed = (deltaTime*1000)/this.time;
        this.f += (b-a)*deltaTransformed;
        if(a < b) {
            if(this.f >= b) {
                this.f = b;
                stop();
            }
        }
        if(a > b) {
            if(this.f <= b) {
                this.f = b;
                stop();
            }
        }
        if(a == b) {
            this.f = b;
            stop();
        }
        this.update.run(f);
    }
}
