package de.bethibande.launcher.modules;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.modules.loader.IModuleHandle;
import de.bethibande.launcher.modules.loader.IModuleLoader;
import de.bethibande.launcher.modules.loader.ModuleLoader;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class ModuleManager implements IModuleManager {

    @Getter
    private IModuleLoader moduleLoader;

    @Getter
    private final File modulePath;
    @Getter
    private final File configPath;
    @Getter
    private final String moduleDescriptionFile;

    public ModuleManager(File _modulePath, File _configPath, String _moduleDescriptionFile) {
        modulePath = _modulePath;
        configPath = _configPath;
        moduleDescriptionFile = _moduleDescriptionFile;
    }

    @Override
    public void initialize() {
        moduleLoader = new ModuleLoader(modulePath, configPath, moduleDescriptionFile);
        moduleLoader.initModuleDirectories();
        moduleLoader.collectAvailableModules();
    }

    @Override
    public void start() {
        moduleLoader.injectCollectedModules();
        moduleLoader.enableAllModules();
        Core.eventManager.runEvent(new ModulesLoadedEvent());
    }

    @Override
    public void stop() {
        moduleLoader.disableAllModules();
    }

    @Override
    public List<IModuleHandle> getHandles() {
        return moduleLoader.getHandles();
    }

    @Override
    public IModule getModuleByName(String name) {
        for(IModuleHandle handle : moduleLoader.getHandles()) {
            if(handle.getDescription().getName().equalsIgnoreCase(name)) return handle.getModule();
        }
        return null;
    }

    @Override
    public void unloadAllModules() {
        moduleLoader.unloadAllModules();
    }
}
