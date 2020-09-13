package de.bethibande.marketplace.modules.loader;

import lombok.NoArgsConstructor;

import java.io.IOException;

@NoArgsConstructor
public class InvalidModuleDescriptionException extends IOException {

    public InvalidModuleDescriptionException(String error) {
        super(error);
    }

}
