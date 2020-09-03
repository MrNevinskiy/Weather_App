package com.hw.weather.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textview.MaterialTextView;
import com.hw.weather.Constants;
import com.hw.weather.OpenWeatherByCoordinates;
import com.hw.weather.R;
import com.hw.weather.fragment.main.weatherForecastView.SourceList;
import com.hw.weather.fragment.main.weatherForecastView.WeatherList;
import com.hw.weather.fragment.weatherRequest.MainWeather;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;
    private OpenWeatherByCoordinates openWeatherByCoordinates;
    private ImageView weatherIcon;

    private void initRetrofitByCoord() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherByCoordinates = retrofit.create(OpenWeatherByCoordinates.class);
    }

    private void requestRetrofitByCoord(String latitude, String longitude) {
        Log.d(TAG, "requestRetrofitByCoordinates: "+ latitude + " ," + longitude + " ," + WEATHER_API_KEY);
        openWeatherByCoordinates.loadWeather(latitude, longitude, WEATHER_API_KEY)
                .enqueue(new Callback<MainWeather>() {
                    @Override
                    public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                        if (response.body() != null) {
                            CurrentWeatherIcon(response);
                            String name = response.body().getName() + ", " + response.body().getSys().getCountry();
                            String temp = String.valueOf(response.body().getMain().getTemp() + absoluteZero).substring(0,2) + "°C";
                            String tempMin = String.valueOf(response.body().getMain().getTempMin() + absoluteZero).substring(0,2) + "°C";
                            String tempMax = String.valueOf(response.body().getMain().getTempMax() + absoluteZero).substring(0,2) + "°C";
                            long press =  response.body().getVisibility().longValue();
                            Double wind = response.body().getWind().getSpeed().doubleValue();
                            String up = Calendar.getInstance().getTime().toString();
                            mSetting = requireContext().getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSetting.edit();
                            editor.putString(APP_PREFERENCES_CITY, name);
                            editor.putString(APP_PREFERENCES_TEMPERATURE, temp);
                            editor.putString(APP_PREFERENCES_TEMPERATURE_MIN, tempMin);
                            editor.putString(APP_PREFERENCES_TEMPERATURE_MAX, tempMax);
                            editor.putString(APP_PREFERENCES_DATE, up);
                            editor.putString(APP_PREFERENCES_UPDATE, up);
                            editor.putString(APP_PREFERENCES_PRESSURE_INFO, String.valueOf(press));
                            editor.putString(APP_PREFERENCES_WIND_SPEED_INFO, String.valueOf(wind));
                            editor.apply();
                            getPrefSetting();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainWeather> call, Throwable throwable) {
                        Log.e(TAG, "onFailure: " + throwable);
                    }
                });
    }

    private void testFragmentResult(){
        getParentFragmentManager().setFragmentResultListener("mapRequest", this, (requestKey, bundle) -> {
            String lat = bundle.getString("lat");
            String lon = bundle.getString("lon");
            Log.d(TAG, "mapRequest: "+ lat + " ," + lon);
            initRetrofitByCoord();
            requestRetrofitByCoord(lat, lon);

        });
    }

    private void CurrentWeatherIcon(Response<MainWeather> response) {
        Picasso.get()
                .load("https://openweathermap.org/img/wn/" + response.body().getWeather().get(0).getIcon() + "@4x.png")
                .into(weatherIcon);
        Picasso.get()
                .load("https://openweathermap.org/img/wn/" + response.body().getWeather().get(0).getIcon() + "@4x.png")
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        weatherIcon.setImageBitmap(bitmap);
                        File directory = requireActivity().getDir("icon", Context.MODE_PRIVATE);
                        File file = new File(directory,"icon.png");
                        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String path = directory.getAbsolutePath();
                        mSetting.edit().putString(APP_PREFERENCES_ICON, path + "\\icon.png").apply();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SourceList weatherList = new SourceList(getResources());
        getCityList(weatherList.build(), view);
        checkCity(view);
        getPrefSetting();
        testFragmentResult();
    }


    private void checkCity(@NonNull View view) {
        ImageButton infoCity = view.findViewById(R.id.infoCity);
        infoCity.setOnClickListener((View view3) -> {
            mSetting = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            String city = (Objects.requireNonNull(mSetting.getString(APP_PREFERENCES_CITY, "City")));
            String url = "https://www.google.ru/search?newwindow=1&q=" + city;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    public void getCityList(SourceList sourceList, View view) {
        RecyclerView recyclerView = getView().findViewById(R.id.weatherListMain);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        WeatherList weatherList = new WeatherList(sourceList);
        recyclerView.setAdapter(weatherList);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.separator));
        recyclerView.addItemDecoration(itemDecoration);
    }

    public void getPrefSetting() {
        mSetting = requireContext().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_NIGHT)) {
            boolean theme = mSetting.getBoolean(APP_PREFERENCES_NIGHT, false);
            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.cityFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_CITY, "City"));
        } if (mSetting.contains(APP_PREFERENCES_ICON)) {
            try {
            ImageView imageView = (ImageView) getView().findViewById(R.id.weatherIcon);
            File file = new File(mSetting.getString(APP_PREFERENCES_ICON, null), "icon.png");
            Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                Log.w("File Not Found Exception",e);
                e.printStackTrace();
            }
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE)) {
            getView().findViewById(R.id.pressureFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED)) {
            getView().findViewById(R.id.windSpeedFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.temperatureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE_MIN)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.temp_min);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE_MIN, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE_MAX)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.temp_max);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE_MAX, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_DATE)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.dateFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_DATE, "DATE"));
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE_INFO)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.pressureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_PRESSURE_INFO, "null"));
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED_INFO)) {
            MaterialTextView textView = (MaterialTextView) getView().findViewById(R.id.windSpeedFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_WIND_SPEED_INFO, "null"));
        }
    }
}