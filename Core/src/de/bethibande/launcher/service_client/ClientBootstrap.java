package de.bethibande.launcher.service_client;

import de.bethibande.launcher.bootstrap.IArgumentParser;
import de.bethibande.launcher.bootstrap.IService;
import de.bethibande.launcher.service_client.Window.WindowManager;
import lombok.Getter;

public class ClientBootstrap implements IService {

    @Getter
    private WindowManager windowManager;

    public void bootstrap(IArgumentParser args) {
        this.windowManager = new WindowManager();
    }

    public String getName() { return "client"; }

}
