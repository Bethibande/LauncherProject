package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapCoordinates implements Serializable {

    @Getter
    private final double lon;
    @Getter
    private final double lat;

}
