package de.bethibande.test;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.events.EventHandler;
import de.bethibande.launcher.events.Listener;
import de.bethibande.launcher.events.TestEvent;
import de.bethibande.launcher.networking.events.ConnectorBufferReceivedEvent;
import de.bethibande.launcher.networking.events.SubServerBufferReceivedEvent;

public class TestListener implements Listener {

    @EventHandler
    public void onTest(TestEvent e) {
        Core.loggerInstance.logMessage("Event executed!");
    }

    @EventHandler
    public void onConnectorBufferReceived(ConnectorBufferReceivedEvent e) {
        Core.loggerInstance.logMessage("[Connector] Buffer received: " + new String(e.getBuffer()));
    }

    @EventHandler
    public void onSubServerBufferReceived(SubServerBufferReceivedEvent e) {
        Core.loggerInstance.logMessage("[Server] Buffer received: " + new String(e.getBuffer()));
    }

}
