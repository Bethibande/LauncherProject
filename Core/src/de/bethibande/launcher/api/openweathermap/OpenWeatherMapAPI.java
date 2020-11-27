package de.bethibande.launcher.api.openweathermap;

import com.google.gson.Gson;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherMapAPI {

    // https://openweathermap.org api

    public static final String apiURL = "http://api.openweathermap.org/data/2.5/weather?";

    @Getter
    private String country;
    @Getter
    private String city;
    @Getter
    private int id;

    private final String apiKey;
    @Getter
    private final String languageCode;
    @Getter
    private final WeatherUnit unit;

    public OpenWeatherMapAPI(int city, String apikey, String languageCode, WeatherUnit unit) {
        this.id = city;
        this.apiKey = apikey;
        this.languageCode = languageCode;
        this.unit = unit;
    }

    public OpenWeatherMapAPI(String country, String city, String apikey, String languageCode, WeatherUnit unit) {
        this.country = country;
        this.city = city;
        this.apiKey = apikey;
        this.languageCode = languageCode;
        this.unit = unit;
    }

    public OpenWeatherMapResponse update() {
        try {
            String url = apiURL + (this.city == null ? "id=" + this.id: "q=" + this.city + "," + this.country);
            url = url + "&lang=" + this.languageCode + "&units=" + this.unit.getUnitID() + "&appid=" + this.apiKey;;
            HttpURLConnection con = (HttpURLConnection)new URL(url).openConnection();
            con.setRequestMethod("POST");
            //con.setRequestProperty("Accept-Encoding", "identity");
            con.addRequestProperty("User-Agent", "Java");
            con.setDoOutput(true);
            con.connect();

            int status = con.getResponseCode();
            if(status == 200 || status == 202) {
                InputStream in = con.getInputStream();
                StringBuilder result = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null){
                    result.append(line);
                }

                con.disconnect();
                String res = result.toString();
                res = res.replace("\"1h\"", "\"oneHour\"");
                res = res.replace("\"3h\"", "\"threeHours\"");

                OpenWeatherMapResponse response = new Gson().fromJson(res, OpenWeatherMapResponse.class);
                response.setJson(res);
                return response;
            }
            con.disconnect();
        } catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public enum WeatherUnit {

        Kelvin("Default"), Celsius("Metric"), Fahrenheit("Imperial");

        @Getter
        private final String unitID;

        WeatherUnit(String unitID) {
            this.unitID = unitID;
        }

    }

}
