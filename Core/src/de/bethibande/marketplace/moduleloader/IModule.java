package de.bethibande.marketplace.moduleloader;

public interface IModule {

    String getName();
    String getVersion();
    void onEnable();
    void onDisable();
    IModuleHandle getHandle();
    IModuleManager getManager();

}
