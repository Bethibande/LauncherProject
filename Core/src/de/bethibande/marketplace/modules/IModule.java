package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.bootstrap.IService;
import de.bethibande.marketplace.modules.configs.IModuleConfigManager;
import de.bethibande.marketplace.modules.IModuleManager;
import de.bethibande.marketplace.modules.loader.IModuleHandle;

public interface IModule extends IService {

    String getName();
    String getVersion();
    void onEnable();
    void onDisable();
    IModuleHandle getHandle();
    IModuleManager getManager();
    IModuleConfigManager getConfigManager();

}
