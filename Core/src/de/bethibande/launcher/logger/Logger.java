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
            // open outputstream and create writer
            writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
            this.file = file;
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // log a normal message
    public void logMessage(String message) {
        // get the date for the time
        Date d = new Date();
        // create the message that will be send to the console and logged
        String logMessage = d.getHours() + ":" + d.getMinutes() + " [Thread/" + Thread.currentThread().getName() + "] " + message;
        // print log message
        System.out.println(logMessage);
        // print message to log file
        logMessageToFile(logMessage);
    }

    // log an error
    public void logError(String error) {
        // get the date for the time
        Date d = new Date();
        // create the message that will be send to the console and logged
        String logMessage = d.getHours() + ":" + d.getMinutes() + " [Error in Thread/" + Thread.currentThread().getName() + "] " + error;
        // print log message
        System.err.println(logMessage);
        // print message to log file
        logMessageToFile(logMessage);
    }

    // print the logged messages/erros to the log file
    private void logMessageToFile(String logMessage) {
        // write log message to file
        writer.println(logMessage);
        // flush stream
        writer.flush();
    }

}
