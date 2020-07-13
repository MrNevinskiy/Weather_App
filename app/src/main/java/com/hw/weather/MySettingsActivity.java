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

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void windSpeed(View view) {
        Log.d(String.valueOf(this), "windSpeed");
    }

    public void pressure(View view) {
        Log.d(String.valueOf(this), "pressure");

    }

    public void nightActivity(View view) {
        Log.d(String.valueOf(this), "nightActivity");
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