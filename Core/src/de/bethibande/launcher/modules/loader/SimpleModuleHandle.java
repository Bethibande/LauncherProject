package de.bethibande.launcher.modules.loader;

import de.bethibande.launcher.modules.IModule;
import de.bethibande.launcher.modules.IModuleDescription;
import lombok.Getter;

import java.io.File;

public class SimpleModuleHandle implements IModuleHandle {

    @Getter
    // the module description
    private final IModuleDescription description;
    @Getter
    // the module main class instance
    private final IModule module;
    @Getter
    // the class loader instance that was used to load this module
    private final StaticClassloader classLoader;
    @Getter
    // the jar file that represents the module
    private final File moduleJarFile;
    @Getter
    // the path/file the configs of this module will be saved in
    private final File moduleConfigPath;

    public SimpleModuleHandle(IModuleDescription description, IModule module, StaticClassloader classLoader, File moduleJarFile, File moduleConfigPath) {
        this.description = description;
        this.module = module;
        this.classLoader = classLoader;
        this.moduleJarFile = moduleJarFile;
        this.moduleConfigPath = moduleConfigPath;
    }

}
