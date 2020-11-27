package de.bethibande.launcher.ui.drawable;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class UiDrawable {

    @Getter
    @Setter
    private float width;
    @Getter
    @Setter
    private float height;
    @Getter
    @Setter
    private float cornerSize;
    @Getter
    @Setter
    private float startingAngle;
    @Getter
    @Setter
    private float endAngle;

    public void draw(Graphics2D g) {

    }

}
