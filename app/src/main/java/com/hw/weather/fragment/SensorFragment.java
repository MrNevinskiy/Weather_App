package com.hw.weather.fragment;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hw.weather.MainActivity;
import com.hw.weather.R;



public class SensorFragment extends Fragment {

    private TextView S_ACCELEROMETER, S_TEMPERATURE, S_HUMIDITY, S_GRAVITY;

    private SensorManager sensorManager;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);

        BottomNavigationView navView = view.findViewById(R.id.nav_view_search);
        navView.getMenu().findItem(R.id.navigation_search).setChecked(true);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        S_ACCELEROMETER = view.findViewById(R.id.S_ACCELEROMETER);
        S_TEMPERATURE = view.findViewById(R.id.S_TEMPERATURE);
        S_HUMIDITY = view.findViewById(R.id.S_HUMIDITY);
        init();
        return view;
    }

    private void init(){
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null){
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).toString();
            S_ACCELEROMETER.setText(" S_ACCELEROMETER" + sensor);
        } else {
            // Failure! No magnetometer.
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null){
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE).toString();
            S_TEMPERATURE.setText("S_TEMPERATURE" + sensor);
        } else {
            // Failure! No magnetometer.
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null){
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY).toString();
            S_HUMIDITY.setText("S_HUMIDITY " + sensor);
        } else {
            // Failure! No magnetometer.
        }
    }

}