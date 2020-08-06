package com.hw.weather.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hw.weather.MainActivity;
import com.hw.weather.R;
import com.hw.weather.fragment.search.SearchFragment;


public class SensorFragment extends Fragment {

    private TextView S_ACCELEROMETER, S_TEMPERATURE, S_HUMIDITY, S_GRAVITY;
    private SensorManager sensorManager;


    BottomNavigationView.OnNavigationItemSelectedListener selectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
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
            }
            return false;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        com.google.android.material.bottomnavigation.BottomNavigationView navView = view.findViewById(R.id.nav_view_search);
        navView.getMenu().findItem(R.id.navigation_search).setChecked(true);
        navView.setOnNavigationItemSelectedListener(selectedListener);

        S_ACCELEROMETER = view.findViewById(R.id.S_ACCELEROMETER);
        S_TEMPERATURE = view.findViewById(R.id.S_TEMPERATURE);
        S_HUMIDITY = view.findViewById(R.id.S_HUMIDITY);
        init();
        return view;
    }

    private void init() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).toString();
            S_ACCELEROMETER.setText(" S_ACCELEROMETER" + sensor);
        } else {
            return;
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE).toString();
            S_TEMPERATURE.setText("S_TEMPERATURE" + sensor);
        } else {
            return;
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY).toString();
            S_HUMIDITY.setText("S_HUMIDITY " + sensor);
        } else {
            return;
        }
    }

}