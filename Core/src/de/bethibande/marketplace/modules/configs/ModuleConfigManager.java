package de.bethibande.marketplace.modules.configs;

import de.bethibande.marketplace.Core;
import de.bethibande.marketplace.modules.IModule;
import de.bethibande.marketplace.utils.DataSerializer;
import de.bethibande.marketplace.utils.FileUtils;
import lombok.Getter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleConfigManager implements IModuleConfigManager, Serializable {

    @Getter
    private transient final IModule owner;
    @Getter
    private final List<IModuleConfig> cachedConfigs = new ArrayList<>();
    @Getter
    private final List<IConfigReference> configs;

    public ModuleConfigManager(IModule owner, List<IConfigReference> references) {
        this.owner = owner;
        this.configs = references;
    }

    @Override
    public IModuleConfig createSimpleConfig(String name) {
        if(!configExists(name)) {
            SimpleModuleConfig smc = new SimpleModuleConfig(name, this, owner);
            createModuleConfigPath();
            File configFile = new File(owner.getHandle().getModuleConfigPath() + "/" + name + ".yml");
            FileUtils.createFile(configFile);
            smc.load(configFile);
            cachedConfigs.add(smc);

            ConfigReference cr = new ConfigReference(configFile, name, ConfigType.SIMPLE_CONFIG);
            configs.add(cr);

            return smc;
        } else return null;
    }

    @Override
    public IModuleConfig createGsonConfig(String name) {
        if(!configExists(name)) {
            GsonModuleConfig gmc = new GsonModuleConfig(name, this, owner);
            createModuleConfigPath();
            File configFile = new File(owner.getHandle().getModuleConfigPath() + "/" + name + ".yml");
            FileUtils.createFile(configFile);
            gmc.load(configFile);
            cachedConfigs.add(gmc);

            ConfigReference cr = new ConfigReference(configFile, name, ConfigType.GSON_CONFIG);
            configs.add(cr);

            return gmc;
        } else return null;
    }

    @Override
    public boolean configExists(String name) {
        for(IConfigReference config : configs) {
            if(config.getConfigName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    @Override
    public boolean isConfigInCache(String name) {
        for(IModuleConfig config : cachedConfigs) {
            if(config.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    @Override
    public void loadConfigToCache(IConfigReference reference) {
        if(reference.getType() == ConfigType.GSON_CONFIG) {
            GsonModuleConfig gmc = new GsonModuleConfig(reference.getConfigName(), this, getOwner());
            gmc.load(reference.getConfigReference());
            cachedConfigs.add(gmc);
        }
        if(reference.getType() == ConfigType.SIMPLE_CONFIG) {
            SimpleModuleConfig smc = new SimpleModuleConfig(reference.getConfigName(), this, getOwner());
            smc.load(reference.getConfigReference());
            cachedConfigs.add(smc);
        }
        System.out.println("loaded config to cache: " + reference.getConfigReference());
    }

    private void createModuleConfigPath() {
        if(!owner.getHandle().getModuleConfigPath().exists()) {
            if(!owner.getHandle().getModuleConfigPath().mkdir()) {
                Core.loggerInstance.logError("Couldn't save module config manager from module: " + owner.getName());
            }
        }
    }

    @Override
    public IConfigReference getReferenceByName(String name) {
        for(IConfigReference ref : configs) {
            if(ref.getConfigName().equalsIgnoreCase(name)) return ref;
        }
        return null;
    }

    @Override
    public void save() {
        File saveFile = new File(owner.getHandle().getModuleConfigPath() + "/configs.ser");
        createModuleConfigPath();
        FileUtils.createFile(saveFile);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(saveFile));
            String ser = DataSerializer.serialize(this.configs);
            pw.println(ser);
            pw.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfigAndRemoveFromCache(IModuleConfig config) {
        config.save();
        this.cachedConfigs.remove(config);
    }

    @Override
    public void removeConfigFromCache(IModuleConfig config) {
        this.cachedConfigs.remove(config);
    }

    @Override
    public IModuleConfig getConfigByName(String name) {
        if(isConfigInCache(name)) {
            for(IModuleConfig config : cachedConfigs) {
                if(config.getName().equalsIgnoreCase(name)) return config;
            }
            return null;
        } else {
            if(configExists(name)) {
                loadConfigToCache(getReferenceByName(name));
                return getConfigByName(name);
            }
        }
        return null;
    }

    @Override
    public void deleteConfig(String name) {
        IModuleConfig cfg = getConfigByName(name);
        if(cfg != null) {
            File cfgFile = cfg.getLoadedFile();
            if(!cfgFile.delete()) {
                Core.loggerInstance.logError("Couldn't delete config file: " + cfgFile);
            } else {
                removeConfigFromCache(cfg);
                configs.remove(getReferenceByName(name));
            }
        } else Core.loggerInstance.logError("Couldn't delete config because it couldn't be found: " + name);
    }

    @Override
    public void saveAll() {
        for(IModuleConfig cfg : this.cachedConfigs) {
            cfg.save();
        }
    }
}
