package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.moduleloader.IModule;
import de.bethibande.marketplace.utils.configs.ISimpleConfig;

public interface IModuleConfig extends ISimpleConfig {

    IModule getOwner();
    IModuleConfigManager getManager();
    String getName();

}
