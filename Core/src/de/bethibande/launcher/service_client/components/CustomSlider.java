package de.bethibande.launcher.service_client.components;

import de.bethibande.launcher.service_client.Window.IWindowHandle;
import de.bethibande.launcher.service_client.Window.animations.RatioAnimator;
import de.bethibande.launcher.utils.WindowUtility;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomSlider extends JCheckBox {

    @Getter
    @Setter
    private Color undergroundColor;

    @Getter
    @Setter
    private Color activeColor = Color.green.darker();
    @Getter
    @Setter
    private Color inactiveColor = Color.red.darker();
    @Getter
    @Setter
    private int animationSpeed = 250;
    @Getter
    @Setter
    private int outlineSize = 1;
    @Getter
    @Setter
    private IWindowHandle windowHandle;

    private float switchAnimation = 0;

    public CustomSlider() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(windowHandle == null) return;
                if(isSelected()) {
                    RatioAnimator animator = new RatioAnimator(0.0f, 1.0f, animationSpeed, f -> switchAnimation = f);
                    windowHandle.getRenderer().startAnimator(animator);
                } else {
                    RatioAnimator animator = new RatioAnimator(1.0f, 0.0f, animationSpeed, f -> switchAnimation = f);
                    windowHandle.getRenderer().startAnimator(animator);
                }
            }
        });
    }

    @Override
    public void setBorder(Border border) { }

    @Override
    protected void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D)g2;
        if(undergroundColor != null) {
            WindowUtility.setRenderingHintsAndClear(g, this.undergroundColor);
        } else WindowUtility.setRenderingHints(g);

        GradientPaint paint = new GradientPaint(getWidth()-(getWidth()*2*switchAnimation),(float)getHeight()/2, this.inactiveColor, getWidth()*2-(getWidth()*2*switchAnimation),(float)getHeight()/2,this.activeColor);
        g.setPaint(paint);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        g.setColor(getForeground());
        g.setStroke(new BasicStroke(outlineSize));
        g.drawRoundRect(outlineSize/2, outlineSize/2, getWidth()-outlineSize, getHeight()-outlineSize, getHeight()-outlineSize*2, getHeight()-outlineSize*2);

        int ovalSize = getHeight()-(outlineSize*4);
        g.fillOval((int)((float)(getWidth()-(outlineSize*4)-ovalSize)*switchAnimation)+outlineSize*2, outlineSize*2, ovalSize, ovalSize);
    }
}
