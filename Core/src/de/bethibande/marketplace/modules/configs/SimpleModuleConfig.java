package de.bethibande.marketplace.modules.configs;

import de.bethibande.marketplace.modules.IModule;
import de.bethibande.marketplace.utils.configs.SimpleConfig;
import lombok.Getter;

import java.io.Serializable;

public class SimpleModuleConfig extends SimpleConfig implements IModuleConfig, Serializable {

    @Getter
    private transient final IModuleConfigManager manager;
    @Getter
    private transient final IModule owner;
    @Getter
    private final String name;

    public SimpleModuleConfig(String name, IModuleConfigManager manager, IModule owner) {
        this.manager = manager;
        this.owner = owner;
        this.name = name;
    }

}
