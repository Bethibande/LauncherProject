package de.bethibande.launcher.modules.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.File;

@AllArgsConstructor
public class ConfigReference implements IConfigReference {

    @Getter
    private final File configReference;
    @Getter
    private final String configName;
    @Getter
    private final ConfigType type;
}
