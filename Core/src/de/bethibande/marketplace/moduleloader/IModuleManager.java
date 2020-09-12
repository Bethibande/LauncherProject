package de.bethibande.marketplace.moduleloader;

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
    // unload all modules (not recommended, rather restart application)
    void unloadAllModules();

}
