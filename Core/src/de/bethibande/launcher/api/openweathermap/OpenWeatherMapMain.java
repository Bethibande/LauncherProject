package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapMain implements Serializable {

    @Getter
    public final float temp;
    @Getter
    private final float feels_like;
    @Getter
    private final float temp_min;
    @Getter
    private final float temp_max;
    @Getter
    private final int pressure;
    @Getter
    private final int humidity;

}
