package com.hw.weather.fragment.search;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hw.weather.Constants;
import com.hw.weather.R;
import com.hw.weather.SelectedFragment;
import com.hw.weather.fragment.main.MainFragment;
import com.hw.weather.fragment.weatherRequest.MainWeather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


public class SearchFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;
    private AdapterSearchHistoric adapterSearchHistoric;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view_search);
        navView.getMenu().findItem(R.id.navigation_search).setChecked(true);
        navView.setOnNavigationItemSelectedListener(selectedListener);
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

    private void getWeatherFromServer(View view) {
        TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
        String city = searchCity.getEditText().getText().toString();
        try {
            String country = "RU";
            String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country + "&appid=" + WEATHER_API_KEY;
            final URL uri = new URL(WEATHER_URL);
            final Handler handler = new Handler();
            new Thread(() -> {
                HttpsURLConnection urlConnection = null;
                try {
                    urlConnection = (HttpsURLConnection) uri.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(10000);
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); // читаем  данные в поток
                    String result = getLines(in);
                    Gson gson = new Gson();
                    MainWeather weatherRequest = gson.fromJson(result, MainWeather.class);
                    handler.post(() -> saveSearchSetting(weatherRequest));
                } catch (Exception e) {
                    Snackbar.make(view, "", BaseTransientBottomBar.LENGTH_SHORT).show();
                    Log.e(TAG, "Error Connection", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Snackbar.make(view, "Error URL", BaseTransientBottomBar.LENGTH_SHORT).show();
            Log.e(TAG, "Error URL", e);
            e.printStackTrace();
        }
    }

    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private void saveSearchSetting(MainWeather MainWeather) {
        TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
        String city = searchCity.getEditText().getText().toString();
        Double temp = MainWeather.getMain().getTemp() - 273.15;
        long press = MainWeather.getVisibility().longValue();
        Double wind = MainWeather.getWind().getSpeed().doubleValue();
        String up = MainWeather.getTimezone().toString();
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_CITY, city);
        editor.putString(APP_PREFERENCES_TEMPERATURE, String.valueOf(temp));
        editor.putString(APP_PREFERENCES_DATE, up);
        editor.putString(APP_PREFERENCES_UPDATE, up);
        editor.putString(APP_PREFERENCES_PRESSURE_INFO, String.valueOf(press));
        editor.putString(APP_PREFERENCES_WIND_SPEED_INFO, String.valueOf(wind));
        editor.apply();
        Snackbar.make(getView(), "Update" + String.valueOf(temp), BaseTransientBottomBar.LENGTH_LONG).show();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = item -> {
        ((SelectedFragment) requireContext()).NavigationItemSelected(item);
        return false;
    };

    private void init() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.weatherListSearch);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setLayoutManager(layoutManager);
        adapterSearchHistoric = new AdapterSearchHistoric(initData(), this.getActivity());
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
                            ((SelectedFragment) requireContext()).startFragment(new MainFragment());
                        }).show());
    }

    private void enterCity(@NonNull View view) {
        MaterialButton checkCity = view.findViewById(R.id.searchCityFragment);
        checkCity.setOnClickListener((View view1) -> {
            TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
            String city = searchCity.getEditText().getText().toString();
            adapterSearchHistoric.addItem(city);
            getWeatherFromServer(view);
        });
    }

    public void getSearchSetting() {
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        if (mSetting.contains(APP_PREFERENCES_CITY)) {
            TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
            searchCity.getEditText().setText(mSetting.getString(APP_PREFERENCES_CITY, " "));
        }
    }
}