package com.hw.weather.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {WeatherCity.class}, version = 1)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao getCityDao();
}
