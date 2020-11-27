package de.bethibande.launcher.service_client.components;

import de.bethibande.launcher.utils.WindowUtility;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public class Progressbar extends JPanel {

    @Getter
    @Setter
    private int cornerSize;
    @Getter
    @Setter
    private int progress;
    @Getter
    @Setter
    private int maxProgress = 100;
    @Getter
    @Setter
    private float alpha;

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        WindowUtility.setRenderingHints(g2);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerSize, cornerSize);

        if(progress > maxProgress) progress = maxProgress;
        float percent = (float)progress/(float)maxProgress;
        int length = (int)((float)getWidth()*percent);
        g2.setColor(getForeground());
        g2.fillRoundRect(0, 0, length, getHeight(), cornerSize, cornerSize);
    }

}
