package de.bethibande.marketplace.modules.configs;

import de.bethibande.marketplace.modules.IModule;
import de.bethibande.marketplace.utils.configs.ISimpleConfig;

public interface IModuleConfig extends ISimpleConfig {

    IModule getOwner();
    IModuleConfigManager getManager();
    String getName();

}
