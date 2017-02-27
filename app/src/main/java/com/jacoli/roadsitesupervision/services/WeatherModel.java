package com.jacoli.roadsitesupervision.services;

/**
 * Created by lichuange on 17/2/27.
 */

public class WeatherModel extends MsgResponseBase {
    private String Weather;
    private String AirTep;

    public String getWeather() {
        return Weather;
    }

    public void setWeather(String weather) {
        Weather = weather;
    }

    public String getAirTep() {
        return AirTep;
    }

    public void setAirTep(String airTep) {
        AirTep = airTep;
    }
}
