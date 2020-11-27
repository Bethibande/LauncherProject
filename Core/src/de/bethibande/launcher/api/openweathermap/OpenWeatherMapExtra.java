package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapExtra implements Serializable {

    @Getter
    private final String country;
    @Getter
    private final long sunrise;
    @Getter
    private final long sunset;

}
