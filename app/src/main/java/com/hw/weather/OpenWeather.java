package com.hw.weather;

import com.hw.weather.fragment.weatherRequest.MainWeather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenWeather {
    @GET("data/2.5/weather")
    Call<MainWeather> loadWeather(@Query("q") String cityCountry, @Query("appid") String keyApi);
}
