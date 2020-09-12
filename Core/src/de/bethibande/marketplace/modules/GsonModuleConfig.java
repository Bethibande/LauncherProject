package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.moduleloader.IModule;
import de.bethibande.marketplace.utils.configs.GsonConfig;
import lombok.Getter;

public class GsonModuleConfig extends GsonConfig implements IModuleConfig {

    @Getter
    private final IModuleConfigManager manager;
    @Getter
    private final IModule owner;
    @Getter
    private final String name;

    public GsonModuleConfig(String name, IModuleConfigManager manager, IModule owner) {
        this.manager = manager;
        this.owner = owner;
        this.name = name;
    }

}
