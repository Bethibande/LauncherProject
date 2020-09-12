package de.bethibande.marketplace;

import de.bethibande.marketplace.bootstrap.ArgumentParser;
import de.bethibande.marketplace.bootstrap.IArgumentParser;
import de.bethibande.marketplace.service_client.ClientBootstrap;
import de.bethibande.marketplace.bootstrap.IService;
import de.bethibande.marketplace.service_server.ServerBootstrap;
import de.bethibande.marketplace.logger.ILogger;
import de.bethibande.marketplace.logger.Logger;
import de.bethibande.marketplace.modules.IModuleManager;
import de.bethibande.marketplace.modules.ModuleManager;

import java.io.File;

public class Core {

    public static File logFile;
    public static ILogger loggerInstance;
    public static IArgumentParser argumentParser;

    public static IService bootstrapInstance;
    public static IModuleManager moduleManager;

    public static void main(String[] args) {
        argumentParser = new ArgumentParser();
        argumentParser.parseArguments(args);
        if(!argumentParser.hasArgument("service")) System.exit(IService.EXIT_NO_SERVICE_SPECIFIED);
        if(argumentParser.getArgumentValue("service").equalsIgnoreCase("server")) {
            logFile = new File("server_log.txt");
            loggerInstance = new Logger();
            loggerInstance.initLogFile(logFile);
            loggerInstance.logMessage("Starting service: server..");
            bootstrapInstance = new ServerBootstrap();
            bootstrapInstance.bootstrap(argumentParser);
        } else if(argumentParser.getArgumentValue("service").equalsIgnoreCase("client")) {
            logFile = new File("client_log.txt");
            loggerInstance = new Logger();
            loggerInstance.initLogFile(logFile);
            loggerInstance.logMessage("Starting service: client..");
            bootstrapInstance = new ClientBootstrap();
            bootstrapInstance.bootstrap(argumentParser);
        } else System.exit(IService.EXIT_NO_SERVICE_SPECIFIED);

        loggerInstance.logMessage("Service started!");
        loggerInstance.logMessage("Starting module loader...");

        moduleManager = new ModuleManager();
        moduleManager.initialize();
        moduleManager.start();

        loggerInstance.logMessage("The module loader has finished and loaded all loadable modules!");

        loggerInstance.logMessage("Bootstrap finished!");
    }

    public static void shutdown(int exitCode) {
        loggerInstance.logMessage("Stopping module manager");
        moduleManager.stop();
        loggerInstance.logMessage("Stopped and saved all modules");

        loggerInstance.logMessage("Exiting with exit code: " + exitCode + "!");
        System.exit(0);
    }

}
