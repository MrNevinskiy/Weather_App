package com.hw.weather.fragment.search;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hw.weather.Constants;
import com.hw.weather.OpenWeather;
import com.hw.weather.R;
import com.hw.weather.SupportItemSelect;
import com.hw.weather.fragment.main.MainFragment;
import com.hw.weather.fragment.weatherRequest.MainWeather;
import com.hw.weather.room.App;
import com.hw.weather.room.WeatherCity;
import com.hw.weather.room.WeatherDao;
import com.hw.weather.room.WeatherSource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SearchFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;
    private AdapterSearchHistoric adapterSearchHistoric;
    private OpenWeather openWeather;
    private WeatherSource weatherSource;
//    private FragmentMainBinding binding;
    String country;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
//        binding = FragmentMainBinding.inflate(inflater, container, false);
//        View view = binding.getRoot();
        BottomNavigationView navView = view.findViewById(R.id.nav_view_search);
        navView.getMenu().findItem(R.id.navigation_search).setChecked(true);
        navView.setOnNavigationItemSelectedListener(selectedListener);
        initRetrofit();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSearchSetting();
        init();
        saveCityInfo(view);
        enterCity(view);
    }

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
                            String up = Calendar.getInstance().getTime().toString();
                            Double temp = response.body().getMain().getTemp() + absoluteZero;
                            WeatherCity newWeatherCity = new WeatherCity();
                            newWeatherCity.city = country;
                            newWeatherCity.date = up;
                            newWeatherCity.temp = temp.toString();
                            weatherSource.addCity(newWeatherCity);
                            adapterSearchHistoric.notifyDataSetChanged();
                            saveSearchSetting(response);
                            Snackbar.make(view, cityCountry, BaseTransientBottomBar.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainWeather> call, Throwable t) {
                        Snackbar.make(view, "Error", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveSearchSetting(Response<MainWeather> response) {
        Double temp = response.body().getMain().getTemp() + absoluteZero;
        long press = response.body().getVisibility().longValue();
        Double wind = response.body().getWind().getSpeed().doubleValue();
        String up = response.body().getTimezone().toString();
        mSetting = requireContext().getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_CITY, country);
        editor.putString(APP_PREFERENCES_TEMPERATURE, String.valueOf(temp));
        editor.putString(APP_PREFERENCES_DATE, up);
        editor.putString(APP_PREFERENCES_UPDATE, up);
        editor.putString(APP_PREFERENCES_PRESSURE_INFO, String.valueOf(press));
        editor.putString(APP_PREFERENCES_WIND_SPEED_INFO, String.valueOf(wind));
        editor.apply();
        Snackbar.make(getView(), "Update" + String.valueOf(temp), BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = item -> {
        ((SupportItemSelect) requireContext()).NavigationItemSelected(item);
        return false;
    };

    private void init() {
        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.weatherListSearch);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        WeatherDao weatherDao = App.getInstance().getWeatherDao();
        weatherSource = new WeatherSource(weatherDao);

        adapterSearchHistoric = new AdapterSearchHistoric(weatherSource,this);
        recyclerView.setAdapter(adapterSearchHistoric);
    }

    private List<String> initData() {
        List<String> list = new ArrayList<>();
        list.add("Bishkek");
        list.add("Moscow");
        return list;
    }

    private void saveCityInfo(@NonNull View view) {
        MaterialButton saveCity = view.findViewById(R.id.saveButtonSearch);
        saveCity.setOnClickListener(view1 ->
                Snackbar.make(view1, "Вы выбрали новый город.", Snackbar.LENGTH_LONG)
                        .setAction(save, view2 -> {
                            ((SupportItemSelect) requireContext()).startFragment(new MainFragment());
                        }).show());
    }

    private void enterCity(@NonNull View view) {
        MaterialButton checkCity = view.findViewById(R.id.searchCityFragment);
        checkCity.setOnClickListener((View view1) -> {
            TextInputLayout searchCity = (TextInputLayout) view.findViewById(R.id.entryCityFragment);
            country = searchCity.getEditText().getText().toString();
            requestRetrofit(country,WEATHER_API_KEY,view);
        });
    }

    public void getSearchSetting() {
        mSetting = requireContext().getSharedPreferences(APP_PREFERENCES,Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            TextInputLayout searchCity = (TextInputLayout) getView().findViewById(R.id.entryCityFragment);
            searchCity.getEditText().setText(mSetting.getString(APP_PREFERENCES_CITY, " "));
        }
    }
}