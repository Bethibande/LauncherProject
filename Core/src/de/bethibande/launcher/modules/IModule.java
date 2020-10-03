package de.bethibande.launcher.modules;

import de.bethibande.launcher.bootstrap.IService;
import de.bethibande.launcher.modules.configs.IModuleConfigManager;
import de.bethibande.launcher.modules.loader.IModuleHandle;

public interface IModule extends IService {

    String getName();
    String getVersion();
    void onEnable();
    void onDisable();
    IModuleHandle getHandle();
    IModuleManager getManager();
    IModuleConfigManager getConfigManager();

}
