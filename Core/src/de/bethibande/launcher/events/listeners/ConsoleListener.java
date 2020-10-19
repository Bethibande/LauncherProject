package de.bethibande.launcher.events.listeners;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.events.ConsoleInputEvent;
import de.bethibande.launcher.events.EventManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleListener extends Thread {

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String s;
        try {
            while((s = reader.readLine()) != null) {
                String[] args = s.split(" ");

                Core.eventManager.runEvent(new ConsoleInputEvent(s, args));
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
