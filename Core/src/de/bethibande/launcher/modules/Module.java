package de.bethibande.launcher.modules;

import de.bethibande.launcher.bootstrap.IArgumentParser;
import de.bethibande.launcher.modules.configs.IModuleConfigManager;
import de.bethibande.launcher.modules.loader.IModuleHandle;
import jdk.jshell.spi.ExecutionControl;
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
        //throw new NotImplementedException();
    }

    public void onDisable() {
        //throw new NotImplementedException();
    }

    @Override
    public void bootstrap(IArgumentParser args) {

    }

    public String getName() { return this.description.getName(); }
    public String getVersion() { return this.description.getVersion(); }

}
