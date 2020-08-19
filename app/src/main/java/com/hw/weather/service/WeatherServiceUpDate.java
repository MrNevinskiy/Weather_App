package com.hw.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;

import com.hw.weather.Constants;
import com.hw.weather.OpenWeatherByName;
import com.hw.weather.fragment.weatherRequest.MainWeather;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherServiceUpDate extends Service implements Constants {

    private final ServiceBinder binder = new ServiceBinder();
    private OpenWeatherByName openWeatherByName;
    private ImageView weatherIcon;
    private MainWeather mainWeather;
    private String city= "Moscow";
    private String country = "RU";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyService onCreate");
        initRetrofit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "MyService onDestroy");
    }

    private void initRetrofit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherByName = retrofit.create(OpenWeatherByName.class);
    }

    private void CurrentWeatherIcon(Response<MainWeather> response) {
        Picasso.get()
                .load("https://i.ibb.co/0D17WKS/night-fog.png")
                .into(weatherIcon);
    }

    private void requestRetrofit(String cityCountry, String keyApi){
        openWeatherByName.loadWeather(cityCountry, keyApi)
                .enqueue(new Callback<MainWeather>() {
                    @Override
                    public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                        if(response.body() != null){
                            CurrentWeatherIcon(response);
                            Double temp = response.body().getMain().getTemp() + absoluteZero;
                            Log.d(TAG, temp.toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<MainWeather> call, Throwable t) {
                        Log.d(TAG, "requestRetrofit onFailure");
                    }
                });
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind");
        return binder;
    }

    public class ServiceBinder extends Binder {

        public WeatherServiceUpDate getService() {
            return WeatherServiceUpDate.this;
        }

        public MainWeather getWeatherRequest() {
            return null;
        }
    }
}
