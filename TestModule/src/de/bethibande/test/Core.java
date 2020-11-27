package de.bethibande.test;

import de.bethibande.launcher.events.TestEvent;
import de.bethibande.launcher.modules.Module;
import de.bethibande.launcher.modules.configs.GsonModuleConfig;
import de.bethibande.launcher.modules.configs.SimpleModuleConfig;
import de.bethibande.launcher.networking.connector.SimpleServerConnector;
import de.bethibande.launcher.networking.encryption.RSA;
import de.bethibande.launcher.networking.events.ConnectorBufferReceivedEvent;
import de.bethibande.launcher.networking.server.SimpleNetworkServer;
import de.bethibande.launcher.ui.UiManager;

public class Core extends Module {

    @Override
    public void onEnable() {
        de.bethibande.launcher.Core.loggerInstance.logMessage("Test module has been enabled!!!");

        if(!getConfigManager().configExists("test-config")) {
            getConfigManager().createGsonConfig("test-config");
        }
        if(!getConfigManager().configExists("test-config2")) {
            getConfigManager().createSimpleConfig("test-config2");
        }

        GsonModuleConfig gmc = (GsonModuleConfig)getConfigManager().getConfigByName("test-config");
        SimpleModuleConfig smc = (SimpleModuleConfig)getConfigManager().getConfigByName("test-config2");
        gmc.set("test", "abc2");
        smc.set("test", "abc");
        //getConfigManager().saveAll();

        de.bethibande.launcher.Core.eventManager.registerListener(new TestListener(), this);
        de.bethibande.launcher.Core.eventManager.runEvent(new TestEvent());
        de.bethibande.launcher.Core.loggerInstance.logMessage(smc.get("test") + " " + gmc.get("test"));

        UiManager uiTest = new UiManager();
        uiTest.setScheme(uiTest.loadScheme(getClass().getResourceAsStream("/schemes/test.scheme")));
        de.bethibande.launcher.Core.loggerInstance.logMessage("colorPrimary: " + uiTest.getResource("@color/colorPrimary"));

        int buffer_size = 1024*4;
        boolean encryption = true;
        SimpleNetworkServer sms = new SimpleNetworkServer(22222, buffer_size, encryption, 5000);
        sms.start();

        SimpleServerConnector ssc = new SimpleServerConnector("localhost", 22222, buffer_size, 5000, encryption);
        ssc.payloadReceived(buffer -> {
            de.bethibande.launcher.Core.eventManager.runEvent(new ConnectorBufferReceivedEvent(buffer, ssc));
        });
        ssc.start();

        // wait until the connection is established
        synchronized (ssc) {
            try {
                ssc.wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        ssc.sendBufferToServer("Hallo Server :D".getBytes());
        sms.getSubServers().get(0).sendBufferToClient("Hallo Client :D".getBytes());

        //ssc.disconnect();

    }

}
