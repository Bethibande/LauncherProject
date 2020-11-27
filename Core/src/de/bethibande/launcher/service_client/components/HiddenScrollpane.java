package de.bethibande.launcher.service_client.components;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class HiddenScrollpane extends JPanel {

    @Getter
    @Setter
    private int scrollSpeed = 3;

    public HiddenScrollpane(JPanel view) {
        this.setLayout(new GroupLayout(this));
        this.add(view);
        view.setLocation(0, 0);
        //view.setVisible(false);
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int i = e.getWheelRotation();

                if(view.getY() - i*scrollSpeed <= 0 && view.getY() + view.getHeight() - i*scrollSpeed >= getHeight()) {
                    view.setLocation(0, view.getY() - i*scrollSpeed);
                }
                if(view.getY() - i*scrollSpeed > 0) {
                    view.setLocation(0, 0);
                    return;
                }
                if(view.getY() + view.getHeight() - i*scrollSpeed < getHeight()) {
                    view.setLocation(0, getHeight()-view.getHeight());
                }

            }
        });
    }
}
