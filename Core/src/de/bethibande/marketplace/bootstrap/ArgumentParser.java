package de.bethibande.marketplace.bootstrap;

import java.util.HashMap;

public class ArgumentParser implements IArgumentParser {

    // saves argument key and value
    private HashMap<String, String> arguments = new HashMap<>();

    @Override
    public void parseArguments(String[] args) {
        String[] buffer;
        for(String s : args) {
            if(s.startsWith("--")) {
                if(s.contains(":")) {
                    buffer = s.substring(2).split(":");
                    arguments.put(buffer[0], buffer[1]);
                } else {
                    arguments.put(s.substring(2), null);
                }
            }
        }
    }

    @Override
    public boolean hasArgument(String key) {
        return arguments.containsKey(key);
    }

    @Override
    public String getArgumentValue(String key) {
        return arguments.get(key);
    }
}
