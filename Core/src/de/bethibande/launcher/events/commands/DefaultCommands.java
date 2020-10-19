package de.bethibande.launcher.events.commands;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.events.ConsoleInputEvent;
import de.bethibande.launcher.events.EventHandler;
import de.bethibande.launcher.events.Listener;

import java.io.IOException;

public class DefaultCommands implements Listener {

    @EventHandler
    public void onConsoleInput(ConsoleInputEvent e) {
        String cmd = e.getFullCommand();
        String[] args = e.getArgs();
        switch (args[0]) {
            case "stop":
                if(args.length != 3) {
                    Core.shutdown(0);
                } else {
                    if(args[1].equalsIgnoreCase("-h")) {
                        try {
                            int delay = new Integer(args[2]);
                            Core.loggerInstance.logMessage("Shutting down in '" + delay + "' seconds.");
                            new Thread(() -> {
                                try {
                                    Thread.sleep(delay*1000);
                                } catch(InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                                Core.shutdown(0);
                            }).start();
                        } catch(NumberFormatException ex) {
                            Core.loggerInstance.logMessage("Syntax error: stop (-h delay) | delay in seconds");
                        }
                    } else Core.loggerInstance.logMessage("Syntax error: stop (-h delay) | delay in seconds");
                }
                break;
        }
    }

}
