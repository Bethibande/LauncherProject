package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapRain implements Serializable {

    @Getter
    private final float oneHour;
    @Getter
    private final float threeHours;

}
