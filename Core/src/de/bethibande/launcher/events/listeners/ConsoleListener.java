package de.bethibande.launcher.events.listeners;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.events.ConsoleInputEvent;
import de.bethibande.launcher.events.EventManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleListener extends Thread {

    static {
        ConsoleListener cl = new ConsoleListener();
        cl.start();
        EventManager.staticEventListeners.add(cl);
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s;
        while(true) {
            try {
                s = reader.readLine();
                String[] args = s.split(" ");

                Core.eventManager.runEvent(new ConsoleInputEvent(s, args));

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
