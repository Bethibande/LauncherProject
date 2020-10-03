package de.bethibande.launcher.modules.configs;

import de.bethibande.launcher.modules.IModule;
import de.bethibande.launcher.utils.configs.ISimpleConfig;

public interface IModuleConfig extends ISimpleConfig {

    IModule getOwner();
    IModuleConfigManager getManager();
    String getName();

}
