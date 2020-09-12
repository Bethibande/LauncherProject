package de.bethibande.marketplace.bootstrap;

public interface IArgumentParser {

    // argument format --key or --key:value

    // process the start arguments
    void parseArguments(String[] args);
    // checks if an argument has been specified
    boolean hasArgument(String key);
    // gets the argument value --key:value, returns null if no value given
    String getArgumentValue(String key);

}
