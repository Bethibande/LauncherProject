package de.bethibande.launcher.service_client.Window.animations;

public class RatioAnimator extends SimpleAnimator {

    private final float from;
    private final float to;
    private final float ratio;

    private final IAnimationUpdate update;

    private float f;

    public RatioAnimator(float from, float to, float ratio, IAnimationUpdate update) {
        this.from = from;
        this.to = to;
        this.ratio = ratio;
        this.update = update;

        this.f = from;
    }

    @Override
    public void update(float deltaTime) {
        float r = this.ratio*deltaTime;
        float distance = Math.abs(this.f-this.to);

        if(distance == 0 || distance <= 0.00125f) {
            stop();
            this.f = this.to;
            this.update.run(f);
            if(getFinish() != null) getFinish().run();
            return;
        }

        float step = distance/r;
        if(this.from < this.to) {
            this.f += step;
            if(this.f >= this.to) {
                this.f = this.to;
                if(getFinish() != null) getFinish().run();
                stop();
            }
        }
        if(this.from > this.to) {
            this.f -= step;
            if(this.f <= this.to) {
                this.f = this.to;
                if(getFinish() != null) getFinish().run();
                stop();
            }
        }

        this.update.run(f);
    }
}
