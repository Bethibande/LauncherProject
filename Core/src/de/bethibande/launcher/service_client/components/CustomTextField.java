package de.bethibande.launcher.service_client.components;

import de.bethibande.launcher.service_client.Window.IWindowHandle;
import de.bethibande.launcher.service_client.Window.animations.IAnimator;
import de.bethibande.launcher.service_client.Window.animations.RatioAnimator;
import de.bethibande.launcher.utils.WindowUtility;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class CustomTextField extends JTextField {

    @Getter
    @Setter
    private Color underlineColor = Color.red;
    @Getter
    @Setter
    private Color inactiveUnderlineColor = Color.lightGray;
    @Getter
    @Setter
    private int underlineHeight = 2;
    @Getter
    @Setter
    private boolean useAnimation = true;
    @Getter
    @Setter
    private int animationSpeed = 500;
    @Getter
    @Setter
    private String textHint;

    private boolean focused = false;
    private float animation = 0.0f;

    @Getter
    @Setter
    private IWindowHandle windowHandle = null;

    private Color alertColor = null;

    public CustomTextField() {
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                focused = true;
                if(alertColor != null) clearAlert();
                if(useAnimation) startAnimation(true);
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                focused = false;
                if(useAnimation) startAnimation(false);
            }
        });
    }

    @Override
    public void setBorder(Border border) { }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        WindowUtility.setRenderingHints(g2);
        g2.setColor(getBackground());
        g2.fillRect(0, 0, getWidth(), getHeight());
        setFont(getFont().deriveFont(getHeight()*0.6f));
        super.paintComponent(g);

        g2.setColor(inactiveUnderlineColor);
        g2.fillRect(0, getHeight()-underlineHeight, getWidth(), underlineHeight);

        if(textHint != null && !focused && getText().isEmpty()) {
            int width = (int)g.getFontMetrics().getStringBounds(textHint, g).getWidth();
            g.drawString(textHint, (getWidth()/2)-(width/2), getHeight()/2);
        }

        if(useAnimation) {
            g2.setColor(underlineColor);
            if(alertColor != null) g2.setColor(alertColor);
            if(animation != 0) g.fillRect(getWidth()/2-(int)(getWidth()*animation)/2, getHeight()-underlineHeight, (int)(getWidth()*animation), underlineHeight);
        }
    }

    public void alert(Color alertColor) {
        this.alertColor = alertColor;
        startAnimation(true);
    }

    public void clearAlert() {
        this.alertColor = null;
    }

    public void startAnimation(boolean a) {
        if(this.windowHandle == null) return;
        IAnimator animator;
        if(a) {
            animator = new RatioAnimator(0.0f, 1.0f, this.animationSpeed, f -> this.animation = f);
        } else {
            animator = new RatioAnimator(1.0f, 0.0f, this.animationSpeed, f -> this.animation = f);
        }
        this.windowHandle.getRenderer().startAnimator(animator);
    }

}
