package de.bethibande.marketplace.logger;

import lombok.Getter;

import java.io.File;

public interface ILogger {

    void initLogFile(File file);

    void logMessage(String message);
    void logError(String error);

}
