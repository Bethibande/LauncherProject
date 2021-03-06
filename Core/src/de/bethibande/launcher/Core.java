package de.bethibande.launcher;

import de.bethibande.launcher.bootstrap.ArgumentParser;
import de.bethibande.launcher.bootstrap.IArgumentParser;
import de.bethibande.launcher.events.EventManager;
import de.bethibande.launcher.events.commands.DefaultCommands;
import de.bethibande.launcher.events.listeners.ConsoleListener;
import de.bethibande.launcher.modules.IModule;
import de.bethibande.launcher.modules.loader.IModuleLoader;
import de.bethibande.launcher.service_client.ClientBootstrap;
import de.bethibande.launcher.bootstrap.IService;
import de.bethibande.launcher.service_server.ServerBootstrap;
import de.bethibande.launcher.logger.ILogger;
import de.bethibande.launcher.logger.Logger;
import de.bethibande.launcher.modules.IModuleManager;
import de.bethibande.launcher.modules.ModuleManager;

import java.io.File;

public class Core {

    public static File logFile;
    public static ILogger loggerInstance;
    public static IArgumentParser argumentParser;

    public static IService bootstrapInstance;
    public static IModuleManager moduleManager;

    public static EventManager eventManager;

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

        loggerInstance.logMessage("Initializing event manager");

        eventManager = new EventManager();

        loggerInstance.logMessage("Starting module loader...");

        moduleManager = new ModuleManager(IModuleLoader.modulesPath, IModuleLoader.moduleConfigPath, IModuleLoader.moduleDescriptionFileName);
        moduleManager.initialize();
        moduleManager.start();

        loggerInstance.logMessage("The module loader has finished and loaded all loadable modules!");

        ConsoleListener listener_console = new ConsoleListener();
        listener_console.start();
        EventManager.staticEventListeners.add(listener_console);
        eventManager.registerListener(new DefaultCommands(), bootstrapInstance);

        loggerInstance.logMessage("Initialized all default commands.");

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
