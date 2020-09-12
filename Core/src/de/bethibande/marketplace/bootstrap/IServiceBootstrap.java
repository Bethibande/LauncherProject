package de.bethibande.marketplace.bootstrap;

public interface IServiceBootstrap {

    int EXIT_COULD_NOT_DELETE_LOG_FILE = 0x100000;
    int EXIT_COULD_NOT_CREATE_LOG_FILE = 0x110000;
    int EXIT_COULD_NOT_CREATE_MODULES_CONFIG_PATH = 0x111000;
    int EXIT_COULD_NOT_CREATE_MODULES_PATH = 0x111100;
    int EXIT_REQUESTED_BY_USER = 0x000000;
    int EXIT_NO_SERVICE_SPECIFIED = 0xffffff;

    void bootstrap(IArgumentParser args);

}
