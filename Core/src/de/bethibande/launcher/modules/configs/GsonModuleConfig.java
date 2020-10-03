package de.bethibande.launcher.modules.configs;

import de.bethibande.launcher.modules.IModule;
import de.bethibande.launcher.utils.configs.GsonConfig;
import lombok.Getter;

import java.io.Serializable;

public class GsonModuleConfig extends GsonConfig implements IModuleConfig, Serializable {

    @Getter
    private transient final IModuleConfigManager manager;
    @Getter
    private transient final IModule owner;
    @Getter
    private final String name;

    public GsonModuleConfig(String name, IModuleConfigManager manager, IModule owner) {
        this.manager = manager;
        this.owner = owner;
        this.name = name;
    }

}
