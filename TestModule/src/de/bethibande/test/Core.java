package de.bethibande.test;

import de.bethibande.launcher.events.TestEvent;
import de.bethibande.launcher.modules.Module;
import de.bethibande.launcher.modules.configs.GsonModuleConfig;
import de.bethibande.launcher.modules.configs.SimpleModuleConfig;
import de.bethibande.launcher.networking.connector.SimpleServerConnector;
import de.bethibande.launcher.networking.encryption.RSA;
import de.bethibande.launcher.networking.events.ConnectorBufferReceivedEvent;
import de.bethibande.launcher.networking.server.SimpleNetworkServer;
import de.bethibande.launcher.networking.webserver.WebServer;
import de.bethibande.launcher.ui.UiManager;
import de.bethibande.launcher.ui.components.UiComponent;
import de.bethibande.launcher.ui.components.UiLabel;
import de.bethibande.launcher.ui.drawable.UiDrawOrder;
import de.bethibande.launcher.ui.drawable.UiDrawable;
import de.bethibande.launcher.ui.drawable.UiGradient;
import de.bethibande.launcher.ui.drawable.UiShape;

import java.io.File;

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

        WebServer webServer = new WebServer(9967, 2048, new File("webserver/"));
        webServer.start();

        new Thread(() -> {
            while(true) {
                de.bethibande.launcher.Core.loggerInstance.logMessage("Average webserver response time: " + webServer.getAverageResponseTime() + " ms");
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) { e.printStackTrace(); }
            }
        }).start();

        UiManager uiTest = new UiManager();
        uiTest.setScheme(uiTest.loadScheme(getClass().getResourceAsStream("/schemes/test.scheme")));
        de.bethibande.launcher.Core.loggerInstance.logMessage("colorPrimary: " + uiTest.getResource("@color/colorPrimary"));

        UiGradient gradient = (UiGradient)uiTest.loadDrawable(getClass().getResourceAsStream("/drawable/gradient.xml"));
        UiShape shape = (UiShape)uiTest.loadDrawable(getClass().getResourceAsStream("/drawable/shape.xml"));
        UiDrawOrder order = (UiDrawOrder)uiTest.loadDrawable(getClass().getResourceAsStream("/drawable/highlightPanel.xml"));
        System.out.println("drawable: id: " + gradient.getId() + " angle: " + gradient.getAngle() + " startColor: " + gradient.getStartColor() + " endColor: " + gradient.getEndColor());
        System.out.println("drawable: id: " + shape.getId() + " shape: " + shape.getShape() + " cornerSize: " + shape.getCornerSize());
        System.out.println("drawable: id: " + order.getId() + " children: ");
        int i = 0;
        for(UiDrawable d : order.getChildren()) {
            System.out.println(i + ": id:" + d.getId());
            i++;
        }

        UiComponent layout = uiTest.loadUiComponent(getClass().getResourceAsStream("/ui/test.ui"));
        System.out.println("layout: id: " + layout.getId() + " background: " + layout.getBackground());
        for(UiComponent child : layout.getChildren()) {
            System.out.println("child: text: " + ((UiLabel)child).getText());
        }

        /*int buffer_size = 1024*4;
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

        //ssc.disconnect();*/

    }

}
