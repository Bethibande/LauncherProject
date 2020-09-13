package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.modules.configs.IModuleConfigManager;
import de.bethibande.marketplace.modules.loader.IModuleHandle;
import lombok.Getter;

public class Module implements IModule {

    @Getter
    private IModuleDescription description;
    @Getter
    private IModuleHandle handle;
    @Getter
    private IModuleManager manager;
    @Getter
    private IModuleConfigManager configManager;

    public void onEnable() {
        // TODO: Override in Module main class to execute code when the module was loaded
    }

    public void onDisable() {
        // TODO: Override in Module main class to execute code when the service is stopping
    }

    public String getName() { return this.description.getName(); }
    public String getVersion() { return this.description.getVersion(); }

}
