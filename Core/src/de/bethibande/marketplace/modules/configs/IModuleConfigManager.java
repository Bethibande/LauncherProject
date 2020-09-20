package de.bethibande.marketplace.modules.configs;

import de.bethibande.marketplace.modules.IModule;

import java.util.List;

public interface IModuleConfigManager {

    IModule getOwner();

    // get existing configs
    List<IConfigReference> getConfigs();
    // get all cached configs
    List<IModuleConfig> getCachedConfigs();
    // SimpleConfig implementation -> less functionality but better performance
    IModuleConfig createSimpleConfig(String name);
    // GsonConfig implementation -> can save all sorts of Objects and not just Strings but has a poorer performance then the SimpleConfig class
    IModuleConfig createGsonConfig(String name);
    // delete a config
    void deleteConfig(String name);
    // save all configs
    void saveAll();
    // check if config already exists
    boolean configExists(String name);
    // checks if the config is cached
    boolean isConfigInCache(String name);
    // loads a config from its file to the cache
    IModuleConfig loadConfigToCache(IConfigReference reference);
    // get config by name, from cache if not loaded in cache it will automatically be loaded to the cache
    IModuleConfig getConfigByName(String name);
    // get config reference from name
    IConfigReference getReferenceByName(String name);
    // save config settings -> loaded/existing configs to configs.json
    void save();
    // save config and remove it from cache
    void saveConfigAndRemoveFromCache(IModuleConfig config);
    // remove config from cache without saving
    void removeConfigFromCache(IModuleConfig config);

}
