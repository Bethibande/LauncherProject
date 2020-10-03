package de.bethibande.launcher.logger;

import java.io.File;

public interface ILogger {

    void initLogFile(File file);

    void logMessage(String message);
    void logError(String error);

}
