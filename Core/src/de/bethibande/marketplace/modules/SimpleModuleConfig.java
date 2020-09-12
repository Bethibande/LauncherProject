package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.moduleloader.IModule;
import de.bethibande.marketplace.utils.configs.SimpleConfig;
import lombok.Getter;

public class SimpleModuleConfig extends SimpleConfig implements IModuleConfig {

    @Getter
    private final IModuleConfigManager manager;
    @Getter
    private final IModule owner;
    @Getter
    private final String name;

    public SimpleModuleConfig(String name, IModuleConfigManager manager, IModule owner) {
        this.manager = manager;
        this.owner = owner;
        this.name = name;
    }

}
