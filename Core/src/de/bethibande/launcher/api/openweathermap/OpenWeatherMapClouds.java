package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapClouds implements Serializable {

    @Getter
    private final int all;

}
