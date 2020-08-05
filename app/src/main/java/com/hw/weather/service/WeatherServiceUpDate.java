package com.hw.weather.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.hw.weather.Constants;
import com.hw.weather.fragment.weatherRequest.MainWeather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

public class WeatherServiceUpDate extends Service implements Constants {

    private final ServiceBinder binder = new ServiceBinder();
    private MainWeather mainWeather;
    private String city= "Moscow";
    private String country = "RU";
    private String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + WEATHER_API_KEY;

    @Override
    public void onCreate() {
        Log.d(TAG, "MyService onCreate");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyService onDestroy");
        super.onDestroy();
    }

    private MainWeather getWeatherRequest() {
            try {
                final URL uri = new URL(WEATHER_URL);
                Thread thread = new Thread(() -> {
                    HttpsURLConnection urlConnection = null;
                    try {
                        urlConnection = (HttpsURLConnection) uri.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.setReadTimeout(10000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                        String result = WeatherServiceUpDate.this.getLines(in);
                        Gson gson = new Gson();
                        mainWeather = gson.fromJson(result, MainWeather.class);
                    } catch (Exception e) {
                        Log.e(TAG, "Error Connection", e);
                        e.printStackTrace();
                    } finally {
                        Log.e(TAG, "Connection");
                        Log.e(TAG, mainWeather.getMain().getTempMax().toString());
                        Log.e(TAG, mainWeather.getMain().getTempMin().toString());
                        if (null != urlConnection) {
                            urlConnection.disconnect();
                        }
                    }
                });
                thread.start();
                thread.join();
            } catch (MalformedURLException | InterruptedException e) {
                Log.e(TAG, "Error URL", e);
                e.printStackTrace();
            }
            return mainWeather;
        }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyService onBind");
        return binder;
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    public class ServiceBinder extends Binder {

        public WeatherServiceUpDate getService() {
            return WeatherServiceUpDate.this;
        }

        public MainWeather getWeatherRequest() {
            return getService().getWeatherRequest();
        }
    }
}
