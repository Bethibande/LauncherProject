package de.bethibande.launcher.logger;

import de.bethibande.launcher.bootstrap.IService;

import java.io.*;
import java.util.Date;

public class Logger implements ILogger {

    private File file;
    private PrintWriter writer;

    // setup the log file
    public void initLogFile(File file) {
        if(file.exists()) {
            if(!file.delete()) {
                System.err.println("Error while initializing the logger: Old log file couldn't be deleted!");
                System.exit(IService.EXIT_COULD_NOT_DELETE_LOG_FILE);
            }
        }
        try {
            if(!file.createNewFile()) {
                System.err.println("Error while initializing the logger: The log file couldn't be created or is already existing!");
                System.exit(IService.EXIT_COULD_NOT_CREATE_LOG_FILE);
            }

            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            this.file = file;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // log a normal message
    public void logMessage(String message) {
        Date d = new Date();
        String logMessage = String.format("%02d", d.getHours()) + ":" + String.format("%02d", d.getMinutes()) + " [Thread/" + Thread.currentThread().getName() + "] " + message;

        System.out.println(logMessage);
        logMessageToFile(logMessage);
    }

    // log an error
    public void logError(String error) {
        Date d = new Date();
        String logMessage = String.format("%02d", d.getHours()) + ":" + String.format("%02d", d.getMinutes()) + " [Error in Thread/" + Thread.currentThread().getName() + "] " + error;

        System.err.println(logMessage);
        logMessageToFile(logMessage);
    }

    // print the logged messages/erros to the log file
    private void logMessageToFile(String logMessage) {
        writer.println(logMessage);
        writer.flush();
    }

}
