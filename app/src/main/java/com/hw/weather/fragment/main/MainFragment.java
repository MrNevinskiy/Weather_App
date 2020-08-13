package com.hw.weather.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.hw.weather.Constants;
import com.hw.weather.OpenWeather;
import com.hw.weather.R;
import com.hw.weather.SelectedFragment;
import com.hw.weather.fragment.main.weatherForecastView.SourceList;
import com.hw.weather.fragment.main.weatherForecastView.WeatherList;
import com.hw.weather.fragment.weatherRequest.MainWeather;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;
    private OpenWeather openWeather;
    private ImageView weatherIcon;
    private MaterialTextView temperatureFragment;
    private MaterialTextView cityFragment;

    private void initRetrofit(){
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeather = retrofit.create(OpenWeather.class);
    }

    private void requestRetrofit(String cityCountry, String keyApi, View view){
        openWeather.loadWeather(cityCountry, keyApi)
                .enqueue(new Callback<MainWeather>() {
                    @Override
                    public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                        if(response.body() != null){
                            CurrentWeatherIcon(response);
                            Double temp = response.body().getMain().getTemp() + absoluteZero;
                            temperatureFragment.setText(String.format("%+.0f", temp));
                            cityFragment.setText("Moscow");
                            Snackbar.make(view, "up", BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainWeather> call, Throwable t) {
                        Snackbar.make(view, "Error", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    private void CurrentWeatherIcon(Response<MainWeather> response) {
        Picasso.get()
                .load("https://i.ibb.co/0D17WKS/night-fog.png")
                .into(weatherIcon);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view_home);
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        navView.setOnNavigationItemSelectedListener(selectedListener);
        weatherIcon = view.findViewById(R.id.weatherIcon);
        temperatureFragment = view.findViewById(R.id.temperatureFragment);
        cityFragment = view.findViewById(R.id.cityFragment);
        initRetrofit();
        requestRetrofit("Moscow,RU", WEATHER_API_KEY, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SourceList weatherList = new SourceList(getResources());
        getCityList(weatherList.build(), view);
        checkCity(view);
        getPrefSetting();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = item -> {
        ((SelectedFragment) requireContext()).NavigationItemSelected(item);
        return false;
    };

    private void checkCity(@NonNull View view) {
        ImageButton infoCity = view.findViewById(R.id.infoCity);
        infoCity.setOnClickListener((View view3) -> {
            mSetting = getActivity().getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            String city = (Objects.requireNonNull(mSetting.getString(APP_PREFERENCES_CITY, "City")));
            String url = "https://www.google.ru/search?newwindow=1&q=" + city;
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
    }

    public void getCityList(SourceList sourceList, View view) {
        RecyclerView recyclerView = getActivity().findViewById(R.id.weatherListMain);
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
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_NIGHT)) {
            boolean theme = mSetting.getBoolean(APP_PREFERENCES_NIGHT, false);
            if (theme) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        }
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            MaterialTextView textView = (MaterialTextView) getActivity().findViewById(R.id.cityFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_CITY, "City"));
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE)) {
            getActivity().findViewById(R.id.pressureFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED)) {
            getActivity().findViewById(R.id.windSpeedFragment).setVisibility((mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true)) ? View.GONE : View.VISIBLE);
        }
        if (mSetting.contains(APP_PREFERENCES_TEMPERATURE)) {
            MaterialTextView textView = (MaterialTextView) getActivity().findViewById(R.id.temperatureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE, "Температура 22°"));
        }
        if (mSetting.contains(APP_PREFERENCES_DATE)) {
            MaterialTextView textView = (MaterialTextView) getActivity().findViewById(R.id.dateFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_DATE, "DATE"));
        }
        if (mSetting.contains(APP_PREFERENCES_PRESSURE_INFO)) {
            MaterialTextView textView = (MaterialTextView) getActivity().findViewById(R.id.pressureFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_PRESSURE_INFO, "Давление 739.00 мм."));
        }
        if (mSetting.contains(APP_PREFERENCES_WIND_SPEED_INFO)) {
            MaterialTextView textView = (MaterialTextView) getActivity().findViewById(R.id.windSpeedFragment);
            textView.setText(mSetting.getString(APP_PREFERENCES_WIND_SPEED_INFO, "Скорость ветра 5 м.с"));
        }
    }
}