package com.hw.weather.fragment.recyclerView;

import com.hw.weather.Constants;

public class WeatherListInfo implements Constants {

    private String data;

    public WeatherListInfo(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}
