package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapWind implements Serializable {

    @Getter
    private final float speed;
    @Getter
    private final float deg;

}
