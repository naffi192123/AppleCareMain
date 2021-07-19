package com.naffi.applecare;

import androidx.lifecycle.LiveData;

public class Repository {
    private Weather weather = new Weather(1,34,"Diver Pulwama","02/08/2021","04:00 AM","08:00 PM","Partly cloudy throughout the day");
    private int history;
    private int diseasesList;
    private int UserProfile;
    public Weather getWeather() {
        return weather;
    }
}

