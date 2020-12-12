package de.bethibande.launcher.networking.webserver;

import java.io.File;
import java.util.HashMap;

public class ServerConfig {

    private final HashMap<Integer, File> errorFiles = new HashMap<>();

    public void registerError(int error, File site) {
        if(this.errorFiles.containsKey(error)) this.errorFiles.remove(error);
        this.errorFiles.put(error, site);
    }

    public File getErrorPage(int error) {
        return this.errorFiles.get(error);
    }

}
