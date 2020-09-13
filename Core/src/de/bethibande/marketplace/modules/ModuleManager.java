package de.bethibande.marketplace.modules;

import de.bethibande.marketplace.modules.loader.IModuleHandle;
import de.bethibande.marketplace.modules.loader.IModuleLoader;
import de.bethibande.marketplace.modules.loader.ModuleLoader;
import lombok.Getter;

import java.util.List;

public class ModuleManager implements IModuleManager {

    @Getter
    private IModuleLoader moduleLoader;

    @Override
    public void initialize() {
        moduleLoader = new ModuleLoader();
        moduleLoader.initModuleDirectories();
        moduleLoader.collectAvailableModules();
    }

    @Override
    public void start() {
        moduleLoader.injectCollectedModules();
        moduleLoader.enableAllModules();
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
