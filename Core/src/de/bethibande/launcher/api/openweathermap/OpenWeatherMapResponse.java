package de.bethibande.launcher.api.openweathermap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
public class OpenWeatherMapResponse implements Serializable {

    @Getter
    @Setter
    private String json;

    @Getter
    private final OpenWeatherMapCoordinates coord;
    @Getter
    private final OpenWeatherMapWeather[] weather;
    @Getter
    private final String base;
    @Getter
    private final OpenWeatherMapMain main;
    @Getter
    private final int visibility;
    @Getter
    private final OpenWeatherMapWind wind;
    @Getter
    private final OpenWeatherMapResponse rain;
    @Getter
    private final OpenWeatherMapClouds clouds;
    @Getter
    private final OpenWeatherMapSnow snow;
    @Getter
    private final OpenWeatherMapExtra sys;
    @Getter
    private final long timezone;
    @Getter
    private final long id;
    @Getter
    private final String name;

}
