package de.bethibande.launcher.networking.logging;

import lombok.Getter;

public enum NetworkPackageSource {

    IN("to server"), OUT("from server");

    @Getter
    private final String description;

    NetworkPackageSource(String description) {
        this.description = description;
    }

}
