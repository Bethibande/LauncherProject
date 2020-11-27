package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapWeather implements Serializable {

    @Getter
    private final int id;
    @Getter
    private final String main;
    @Getter
    private final String description;
    @Getter
    private final String icon;

}
