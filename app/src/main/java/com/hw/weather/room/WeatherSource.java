package com.hw.weather.room;

import java.util.List;

public class WeatherSource {

    private final WeatherDao weatherDao;
    private List<WeatherCity> cities;

    public WeatherSource(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public List<WeatherCity> getCities(){
        if (cities == null){
            loadingCity();
        }
        return cities;
    }

    public void loadingCity(){
        cities = weatherDao.getAllCity();
    }

    public long getCountCity(){
        return weatherDao.getCountCity();
    }

    public void addCity(WeatherCity weatherCity){
        weatherDao.insertCity(weatherCity);
        loadingCity();
    }

    public void updateCity(WeatherCity weatherCity){
        weatherDao.updateCity(weatherCity);
        loadingCity();
    }

    public void removeCity(WeatherCity weatherCity){
        weatherDao.deleteCity(weatherCity);
    }

    public void deleteAllCity(){
        weatherDao.deleteAllCity();
        loadingCity();
    }
}
