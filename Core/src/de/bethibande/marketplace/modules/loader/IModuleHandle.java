package de.bethibande.marketplace.modules.loader;

import de.bethibande.marketplace.modules.IModule;
import de.bethibande.marketplace.modules.IModuleDescription;

import java.io.File;

@SuppressWarnings("unused")
public interface IModuleHandle {

    // the module handle containing the class loader that was used to load the module, the module, the module description, the module jar file and the module config directory

    StaticClassloader getClassLoader();
    IModule getModule();
    IModuleDescription getDescription();
    File getModuleJarFile();
    File getModuleConfigPath();

}