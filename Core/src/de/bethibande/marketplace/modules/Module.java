package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.bootstrap.IArgumentParser;
import de.bethibande.marketplace.modules.configs.IModuleConfigManager;
import de.bethibande.marketplace.modules.loader.IModuleHandle;
import lombok.Getter;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
        //throw new NotImplementedException();
    }

    public void onDisable() {
        //throw new NotImplementedException();
    }

    @Override
    public void bootstrap(IArgumentParser args) {
        throw new NotImplementedException();
    }

    public String getName() { return this.description.getName(); }
    public String getVersion() { return this.description.getVersion(); }

}
