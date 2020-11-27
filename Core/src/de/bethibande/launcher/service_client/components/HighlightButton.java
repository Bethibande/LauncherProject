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
import java.awt.geom.Rectangle2D;

public class HighlightButton extends JButton {

    @Getter
    @Setter
    private Color colorA = Color.decode("#9c37b8");
    @Getter
    @Setter
    private Color colorB = Color.decode("#1a37ad");
    @Getter
    @Setter
    private int cornerSize = 0;
    @Getter
    @Setter
    private int animationSizeChange = 1;

    private float animation = 0;
    @Setter
    private IWindowHandle handle;
    @Getter
    @Setter
    private Color underground;

    public HighlightButton() {
        addEventHandlers();
    }

    public HighlightButton(String text) {
        super(text);
        addEventHandlers();
    }

    private void addEventHandlers() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(handle == null) return;
                RatioAnimator animator = new RatioAnimator(0.0f, 1.0f, 100, f -> animation = f);
                handle.getRenderer().startAnimator(animator);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(handle == null) return;
                RatioAnimator animator = new RatioAnimator(1.0f, 0.0f, 100, f -> animation = f);
                handle.getRenderer().startAnimator(animator);
            }
        });
    }

    @Override
    public void setBorder(Border border) { }

    @Override
    protected void paintComponent(Graphics g2) {
        Graphics2D g = (Graphics2D)g2;
        if(underground == null) {
            WindowUtility.setRenderingHints(g);
        } else WindowUtility.setRenderingHintsAndClear(g, this.underground);
        setFont(getFont().deriveFont((getHeight()-(int)(animationSizeChange*animation))*0.6f));

        GradientPaint gradient = new GradientPaint(0, getHeight()/2f, this.colorA, getWidth(), getHeight()/2f, this.colorB);
        g.setPaint(gradient);

        int corner = this.cornerSize-(int)(animationSizeChange*animation);
        g.fillRoundRect((int)(animationSizeChange*animation), (int)(animationSizeChange*animation), getWidth()-(int)(animationSizeChange*animation*2), getHeight()-(int)(animationSizeChange*animation*2), corner, corner);

        g.setColor(getForeground());
        Rectangle2D size = g.getFontMetrics().getStringBounds(getText(), g);
        int width = (int)size.getWidth();
        int height = (int)size.getHeight();
        g.drawString(getText(), (getWidth()/2)-(width/2), (getHeight()/2)+(getHeight()-height)/2);
    }
}
