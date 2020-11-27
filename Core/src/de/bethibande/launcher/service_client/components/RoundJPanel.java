package de.bethibande.launcher.service_client.components;

import de.bethibande.launcher.utils.WindowUtility;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class RoundJPanel extends JPanel {

    @Getter
    @Setter
    private int cornerSize = 10;
    @Getter
    @Setter
    private Color underground;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        if(this.underground == null) {
            WindowUtility.setRenderingHints(g2);
        } else WindowUtility.setRenderingHintsAndClear(g2, underground);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerSize, cornerSize);
    }

}
