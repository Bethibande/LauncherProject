package de.bethibande.marketplace.modules;

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

}
