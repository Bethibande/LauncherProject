package de.bethibande.marketplace.modules.configs;

import de.bethibande.marketplace.Core;
import de.bethibande.marketplace.modules.IModule;
import de.bethibande.marketplace.utils.DataSerializer;
import de.bethibande.marketplace.utils.FileUtils;
import lombok.Getter;

import java.io.*;
import java.util.List;

public class ModuleConfigManager implements IModuleConfigManager, Serializable {

    @Getter
    private transient final IModule owner;
    @Getter
    // TODO: change so this is only cache and configs are only loaded from file when getConfigByName is called
    private final List<IModuleConfig> configs;

    public ModuleConfigManager(IModule owner, List<IModuleConfig> configs) {
        this.owner = owner;
        this.configs = configs;
    }

    @Override
    public IModuleConfig createSimpleConfig(String name) {
        if(!configExists(name)) {
            SimpleModuleConfig smc = new SimpleModuleConfig(name, this, owner);
            createModuleConfigPath();
            File configFile = new File(owner.getHandle().getModuleConfigPath() + "/" + name + ".yml");
            FileUtils.createFile(configFile);
            smc.load(configFile);
            configs.add(smc);
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
            configs.add(gmc);
            return gmc;
        } else return null;
    }

    @Override
    public boolean configExists(String name) {
        for(IModuleConfig config : configs) {
            if(config.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

    private void createModuleConfigPath() {
        if(!owner.getHandle().getModuleConfigPath().exists()) {
            if(!owner.getHandle().getModuleConfigPath().mkdir()) {
                Core.loggerInstance.logError("Couldn't save module config manager from module: " + owner.getName());
            }
        }
    }

    @Override
    public void save() {
        File saveFile = new File(owner.getHandle().getModuleConfigPath() + "/configs.ser");
        createModuleConfigPath();
        FileUtils.createFile(saveFile);
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(saveFile));
            String ser = DataSerializer.serialize(this);
            pw.println(ser);
            pw.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public IModuleConfig getConfigByName(String name) {
        for(IModuleConfig config : configs) {
            if(config.getName().equalsIgnoreCase(name)) return config;
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
                configs.remove(cfg);
            }
        } else Core.loggerInstance.logError("Couldn't delete config because it couldn't be found: " + name);
    }

    @Override
    public void saveAll() {
        for(IModuleConfig cfg : configs) {
            cfg.save();
        }
    }
}
