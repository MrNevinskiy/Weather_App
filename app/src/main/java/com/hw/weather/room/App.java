package com.hw.weather.room;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    private static App instance;
    private WeatherDatabase db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                WeatherDatabase.class,
                "weather_database")
                .allowMainThreadQueries()
                .build();
    }

    public WeatherDao getWeatherDao() {
        return db.getCityDao();
    }
}
