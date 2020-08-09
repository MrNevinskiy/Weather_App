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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.hw.weather.Constants;
import com.hw.weather.MainActivity;
import com.hw.weather.R;
import com.hw.weather.fragment.setting.MySettingFragment;
import com.hw.weather.fragment.main.weatherForecastView.SourceList;
import com.hw.weather.fragment.main.weatherForecastView.WeatherList;
import com.hw.weather.fragment.search.SearchFragment;
import com.hw.weather.fragment.weatherRequest.MainWeather;

import java.util.Objects;

public class MainFragment extends Fragment implements Constants {

    private SharedPreferences mSetting;
    private MainWeather mainWeather;

    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    MainFragment mainFragment = new MainFragment();
                    ((MainActivity) getActivity()).startFragment(mainFragment);
                    return true;
                case R.id.navigation_setting:
                    MySettingFragment mySettingFragment = new MySettingFragment();
                    ((MainActivity) getActivity()).startFragment(mySettingFragment);
                    return true;
                case R.id.navigation_search:
                    SearchFragment searchFragment = new SearchFragment();
                    ((MainActivity) getActivity()).startFragment(searchFragment);
                    return true;
                case R.id.icon_about:
                    Snackbar.make(getView(), "Developed by MrAlex / Designed by Dimas_sugih from Freepik", Snackbar.LENGTH_LONG).setAction("Перейти",view -> {
                        String url = "http://www.freepik.com";
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }).show();
                    return true;
            }
            return false;
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        BottomNavigationView navView = view.findViewById(R.id.nav_view_home);
        navView.getMenu().findItem(R.id.navigation_home).setChecked(true);
        navView.setOnNavigationItemSelectedListener(selectedListener);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SourceList weatherList = new SourceList(getResources());
        getCityList(weatherList.build(), view);

        getPrefSetting();

        ImageButton infoCity = view.findViewById(R.id.infoCity);
        infoCity.setOnClickListener((View view3) -> {
            mSetting = getActivity().getPreferences(Context.MODE_PRIVATE);
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

//    public void getPrefSetting2() {
//
//           new Thread(() -> {
//               try {
//               Thread.sleep(6000);
//           } catch (InterruptedException e) {
//               Log.e(TAG,"error initService", e);
//               e.printStackTrace();
//           }
////                   MaterialTextView cityFragment = (MaterialTextView) getActivity().findViewById(R.id.cityFragment);
////                   cityFragment.setText(mainWeather.getName());
//
//
////                   MaterialTextView temperatureFragment = (MaterialTextView) getActivity().findViewById(R.id.temperatureFragment);
////                   temperatureFragment.setText(mainWeather.getMain().getTemp().toString());
//
//
////                   MaterialTextView dateFragment = (MaterialTextView) getActivity().findViewById(R.id.dateFragment);
////                   dateFragment.setText(mainWeather.getTimezone());
////
////
////                   MaterialTextView pressureFragment = (MaterialTextView) getActivity().findViewById(R.id.pressureFragment);
////                   pressureFragment.setText(mainWeather.getMain().getPressure());
////
////
////                   MaterialTextView windSpeedFragment = (MaterialTextView) getActivity().findViewById(R.id.windSpeedFragment);
////                   windSpeedFragment.setText(mainWeather.getWind().toString());
//           }).start();
   // }

}