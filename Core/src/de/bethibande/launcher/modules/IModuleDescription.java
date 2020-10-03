package de.bethibande.launcher.modules;

import java.util.HashMap;

public interface IModuleDescription {

    // * = optional

    // the module name
    String getName();
    // the module version *
    String getVersion();
    // the main class of the module (extends IModule)
    String getMainClass();
    // the author of the module *
    String getAuthor();
    // the module description *
    String getDescription();
    // get the service name * (the module will only be enabled if the service name of the Core.bootstrapInstance instance matches this value)
    String getMainService();
    // get custom values/values which are none standard values/keys
    HashMap<String, String> getCustomValues();

}
