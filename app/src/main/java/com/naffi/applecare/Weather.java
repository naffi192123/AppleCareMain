package com.naffi.applecare;

import java.util.Calendar;

public class Weather {
    private int id;
    private int temperature;
    private String location;
    private String dateTime;
    private String sinRiseTime;
    private String sunSetTime;
    private String weatherInfo;


    public Weather(int id, int temperature, String location, String dateTime, String sinRiseTime, String sunSetTime, String weatherInfo) {
        this.id = id;
        this.temperature = temperature;
        this.location = location;
        this.dateTime = dateTime;
        this.sinRiseTime = sinRiseTime;
        this.sunSetTime = sunSetTime;
        this.weatherInfo = weatherInfo;

    }


    public int getId() {
        return id;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getLocation() {
        return location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getSinRiseTime() {
        return sinRiseTime;
    }

    public String getSunSetTime() {
        return sunSetTime;
    }

    public String getWeatherInfo() {
        return weatherInfo;
    }
}
