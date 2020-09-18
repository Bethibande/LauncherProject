package de.bethibande.test;

import de.bethibande.marketplace.modules.Module;
import de.bethibande.marketplace.modules.configs.GsonModuleConfig;
import de.bethibande.marketplace.modules.configs.SimpleModuleConfig;

public class Core extends Module {

    @Override
    public void onEnable() {
        de.bethibande.marketplace.Core.loggerInstance.logMessage("Test module has been enabled!!!");

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

        System.out.println(smc.get("test") + " " + gmc.get("test"));

    }

}
