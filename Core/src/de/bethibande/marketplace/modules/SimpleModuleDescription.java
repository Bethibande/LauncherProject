package de.bethibande.marketplace.modules;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class SimpleModuleDescription implements IModuleDescription {

    @Getter
    private final String name;
    @Getter
    private final String version;
    @Getter
    private final String author;
    @Getter
    private final String mainClass;
    @Getter
    private final String description;
    @Getter
    private final String mainService;


}
