package com.hw.weather.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hw.weather.fragment.main.weatherForecastView.WeatherListInfo;

import java.util.List;

@Dao
public interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCity(WeatherCity weatherCity);

    @Update
    void updateCity(WeatherCity weatherCity);

    @Delete
    void deleteCity(WeatherCity weatherCity);

    @Query("DELETE FROM WeatherCity")
    void deleteAllCity();

    @Query("SELECT * FROM WeatherCity ORDER BY id DESC")
    List<WeatherCity> getAllCity();

    @Query("SELECT COUNT() FROM WeatherCity")
    long getCountCity();

}
