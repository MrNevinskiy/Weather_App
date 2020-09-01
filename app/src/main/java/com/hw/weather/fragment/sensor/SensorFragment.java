package com.hw.weather.fragment.sensor;

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
import com.hw.weather.R;
import com.hw.weather.SupportItemSelect;


public class SensorFragment extends Fragment {

    private TextView accelerometer;
    private TextView temperature;
    private TextView gravity;
    private TextView humidity;
    private SensorManager sensorManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sensor, container, false);
        accelerometer = view.findViewById(R.id.S_ACCELEROMETER);
        temperature = view.findViewById(R.id.S_TEMPERATURE);
        humidity = view.findViewById(R.id.S_HUMIDITY);
        init();
        return view;
    }

    private void init() {
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER).toString();
            accelerometer.setText(" S_ACCELEROMETER" + sensor);
        } else {
            return;
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE).toString();
            temperature.setText("S_TEMPERATURE" + sensor);
        } else {
            return;
        }
        if (sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) != null) {
            String sensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY).toString();
            humidity.setText("S_HUMIDITY " + sensor);
        } else {
            return;
        }
    }

}