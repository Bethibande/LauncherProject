package de.bethibande.launcher.events.commands;

import de.bethibande.launcher.Core;
import de.bethibande.launcher.events.ConsoleInputEvent;
import de.bethibande.launcher.events.EventHandler;
import de.bethibande.launcher.events.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DefaultCommands implements Listener {

    public static final List<String> commands = new ArrayList<>();

    static {
        commands.add("stop (-h delay) | delay in seconds");
        commands.add("help (command) | show a list of available commands");
    }

    @EventHandler
    public void onConsoleInput(ConsoleInputEvent e) {
        String cmd = e.getFullCommand();
        String[] args = e.getArgs();
        switch (args[0].toLowerCase()) {
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
            case "help":
                if(args.length == 2) {
                    for(String command : commands) {
                        if(command.toLowerCase().startsWith(args[1].toLowerCase())) {
                            Core.loggerInstance.logMessage("Help - " + args[1]);
                            Core.loggerInstance.logMessage("  " + command);
                            Core.loggerInstance.logMessage("");
                            return;
                        }
                    }
                    Core.loggerInstance.logMessage("Help - There is no such command: " + args[1]);
                } else {
                    Core.loggerInstance.logMessage("Help - Commands");
                    Core.loggerInstance.logMessage("");
                    for (String command : commands) {
                        Core.loggerInstance.logMessage("  " + command);
                    }
                    Core.loggerInstance.logMessage("");
                }
                break;
        }
    }

}
