package de.bethibande.test;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.events.EventHandler;
import de.bethibande.launcher.events.Listener;
import de.bethibande.launcher.events.TestEvent;

public class TestListener implements Listener {

    @EventHandler
    public void onTest(TestEvent e) {
        Core.loggerInstance.logMessage("Event executed!");
    }

}
