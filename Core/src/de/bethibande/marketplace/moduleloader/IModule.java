package de.bethibande.marketplace.moduleloader;

import de.bethibande.marketplace.modules.IModuleConfigManager;
import de.bethibande.marketplace.modules.IModuleManager;

public interface IModule {

    String getName();
    String getVersion();
    void onEnable();
    void onDisable();
    IModuleHandle getHandle();
    IModuleManager getManager();
    IModuleConfigManager getConfigManager();

}
