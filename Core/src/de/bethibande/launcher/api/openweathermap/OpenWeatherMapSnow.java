package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapSnow implements Serializable {

    @Getter
    private final float oneHour;
    @Getter
    private final float threeHours;

}
