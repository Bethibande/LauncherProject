package de.bethibande.marketplace.modules.configs;

import de.bethibande.marketplace.modules.IModule;

import java.util.List;

public interface IModuleConfigManager {

    IModule getOwner();

    List<IModuleConfig> getConfigs();
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
    // get config by name
    IModuleConfig getConfigByName(String name);
    // save config settings -> loaded/existing configs to configs.json
    void save();

}
