package de.bethibande.launcher.modules.configs;

import java.io.File;
import java.io.Serializable;

public interface IConfigReference extends Serializable {

    // get the file this reference is referring to
    File getConfigReference();
    // get the config name this reference is referring to
    String getConfigName();
    // get the type of the config eg. SimpleModuleConfig.java, GsonModuleConfig.java...
    ConfigType getType();

}
