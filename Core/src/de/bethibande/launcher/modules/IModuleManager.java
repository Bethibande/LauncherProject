package de.bethibande.launcher.modules;

import de.bethibande.launcher.modules.loader.IModuleHandle;
import de.bethibande.launcher.modules.loader.IModuleLoader;

import java.util.List;

public interface IModuleManager {

    // init new IModuleLoader, init the needed directories and collect available modules
    void initialize();
    // inject collected modules and call the onEnable method
    void start();
    // call onDisable for all modules
    void stop();
    // get the handles of all the loaded modules
    List<IModuleHandle> getHandles();
    // get an IModule instance by module name
    IModule getModuleByName(String name);
    // get the module loader instance
    IModuleLoader getModuleLoader();
    // unload all modules (not recommended, rather restart application)
    void unloadAllModules();

}
