package de.bethibande.marketplace.moduleloader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public interface IModuleLoader {

    // the path the module configs will be saved at
    File moduleConfigPath = new File("configs/");
    // the path the modules are located at
    File modulesPath = new File("modules/");

    // create modules and configs directory
    void initModuleDirectories();
    // collect all potential modules from the modules directory
    void collectAvailableModules();
    // inject the collected modules and create the handles
    void injectCollectedModules();
    // run the onEnable methods of the modules/handles
    void enableAllModules();
    // run the onDisable methods of the modules/handles
    void disableAllModules();
    // unload/destroy all the classloaders and modules
    void unloadAllModules();
    // get all the loaded module handles
    List<IModuleHandle> getHandles();

}
