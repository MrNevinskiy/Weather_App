package com.hw.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.Toast;


public class MySettingsActivity extends AppCompatActivity implements Constants {

    SharedPreferences mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toast.makeText(this, onCreate, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate);
        mSetting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSetting.contains(APP_PREFERENCES_PRESSURE)){
            CheckBox checkBox = (CheckBox)findViewById(R.id.pressure);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_PRESSURE, true));
        }
        if(mSetting.contains(APP_PREFERENCES_WIND_SPEED)){
            CheckBox checkBox = (CheckBox)findViewById(R.id.windSpeed);
            checkBox.setChecked(mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED, true));
        }
        if(mSetting.contains(APP_PREFERENCES_NIGHT)){
            Switch sw = (Switch)findViewById(R.id.nightActivity);
            sw.setChecked(mSetting.getBoolean(APP_PREFERENCES_NIGHT, true));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, onStart, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, onResume, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onResume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, onPause, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onPause);
        save();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, onStop, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, onDestroy, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onDestroy);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(APP_PREFERENCES_WIND_SPEED,((CheckBox)findViewById(R.id.windSpeed)).isChecked());
//        outState.putBoolean(APP_PREFERENCES_PRESSURE,((CheckBox)findViewById(R.id.pressure)).isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        savedInstanceState.getBoolean(APP_PREFERENCES_WIND_SPEED, true);
//        savedInstanceState.getBoolean(APP_PREFERENCES_PRESSURE, true);
    }

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void windSpeed(View view) {
        Log.d(String.valueOf(this), "windSpeed");
//        boolean checked1 = ((CheckBox) view).isChecked();
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("WS", checked1);
//        startActivity(intent);
    }

    public void pressure(View view) {
        Log.d(String.valueOf(this), "pressure");
//        boolean checked2 = ((CheckBox) view).isChecked();
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("PS", checked2);
//        startActivity(intent);
    }

    public void nightActivity(View view) {
        Log.d(String.valueOf(this), "nightActivity");
        // TODO: 01.07.2020
    }

    public void saveOnClick(View view) {
        Log.d(String.valueOf(this), "saveOnClick");
        save();
    }

    public void save(){
        boolean checked1 = ((CheckBox)findViewById(R.id.pressure)).isChecked();
        boolean checked2 = ((CheckBox)findViewById(R.id.windSpeed)).isChecked();
        boolean checked3 = ((Switch)findViewById(R.id.nightActivity)).isChecked();
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putBoolean(APP_PREFERENCES_PRESSURE, checked1);
        editor.putBoolean(APP_PREFERENCES_WIND_SPEED, checked2);
        editor.putBoolean(APP_PREFERENCES_NIGHT,checked3);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}