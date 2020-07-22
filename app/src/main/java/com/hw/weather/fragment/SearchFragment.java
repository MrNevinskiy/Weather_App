package com.hw.weather.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;
import com.hw.weather.fragment.httpsRequest.MainWeather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;


public class SearchFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getWeatherFromServer(View view) {
        TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
        String city = searchCity.getEditText().getText().toString();

        try {
            String country = "RU";
            String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "," + country +"&appid=" + WEATHER_API_KEY;
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
                    Snackbar.make(view, "Ошибка соединения", BaseTransientBottomBar.LENGTH_SHORT).show();
                    Log.e(TAG, "Ошибка соединения", e);
                    e.printStackTrace();
                } finally {
                    if (null != urlConnection) {
                        urlConnection.disconnect();
                    }
                }
            }).start();
        } catch (MalformedURLException e) {
            Snackbar.make(view, "Ошибка адресса", BaseTransientBottomBar.LENGTH_SHORT).show();
            Log.e(TAG, "Ошибка адресса", e);
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getLines(BufferedReader in) {
        return in.lines().collect(Collectors.joining("\n"));
    }

    private void saveSearchSetting(MainWeather MainWeather){
        TextInputLayout searchCity = (TextInputLayout) getActivity().findViewById(R.id.entryCityFragment);
        String city = searchCity.getEditText().getText().toString();
        Double temp = MainWeather.getMain().getTemp() - 273.15;
        mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_CITY, city);
        editor.putString(APP_PREFERENCES_TEMPERATURE, String.valueOf(temp));
        editor.putString(APP_PREFERENCES_DATE, "Сегодня");
        editor.putString(APP_PREFERENCES_UPDATE, "Сегодня");
        editor.putString(APP_PREFERENCES_PRESSURE_INFO, "Давление 759.00 мм.");
        editor.putString(APP_PREFERENCES_WIND_SPEED_INFO, "Скорость ветра 2 м.с");
        editor.apply();
        Snackbar.make(getView(), "Update" + String.valueOf(temp), BaseTransientBottomBar.LENGTH_LONG).show();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                ((MainActivity) getActivity()).startFragment(1);
                return true;
            case R.id.navigation_setting:
                ((MainActivity) getActivity()).startFragment(2);
                return true;
            case R.id.navigation_search:
                ((MainActivity) getActivity()).startFragment(3);
                return true;

        }
        return false;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view_search);
        navView.getMenu().findItem(R.id.navigation_search).setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSearchSetting();

        MaterialButton saveCityInfo = view.findViewById(R.id.saveButtonSearch);
        saveCityInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Вы выбрали новый город.", Snackbar.LENGTH_LONG).setAction(save, (View.OnClickListener) view2 -> {
                    ((MainActivity) getActivity()).startFragment(1);
                }).show();
            }
        });

        MaterialButton checkCity = view.findViewById(R.id.searchCityFragment);
        checkCity.setOnClickListener((View.OnClickListener) (View view1) -> {
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