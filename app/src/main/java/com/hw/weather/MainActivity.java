package com.hw.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements Constants {

    SharedPreferences mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String instanceState;
        Date date = new Date();
        Singleton singleton = Singleton.getInstance(date.toString());
        if(savedInstanceState == null){
            instanceState = "Первый запуск";
        }else{
            instanceState = "Повторный запуск";
        }
        Toast.makeText(this, onCreate + instanceState + singleton.value, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate + instanceState + singleton.value);
        setting();
        city();
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

    public void searchOnClick(View view) {
        Intent intent = new Intent(this, SearchActivity.class);
        Toast.makeText(this, "searchOnClick", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "searchOnClick");
        startActivity(intent);
    }

    public void settingOnClick(View view) {
        Intent intent = new Intent(this, MySettingsActivity.class);
        Toast.makeText(this, "settingOnClick", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "settingOnClick");
        startActivity(intent);
    }

    public void setting() {
        Date date = new Date();
        String DATE = date.toString();
        mSetting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSetting.contains(APP_PREFERENCES_PRESSURE)){
            findViewById(R.id.pressure).setVisibility((mSetting.getBoolean(APP_PREFERENCES_PRESSURE,true)) ? View.GONE : View.VISIBLE);
        }
        if(mSetting.contains(APP_PREFERENCES_WIND_SPEED)){
            findViewById(R.id.windSpeed).setVisibility((mSetting.getBoolean(APP_PREFERENCES_WIND_SPEED,true)) ? View.GONE : View.VISIBLE);
        }
        if(mSetting.contains(APP_PREFERENCES_TEMPERATURE)) {
            TextView textView = (TextView) findViewById(R.id.temperature);
            textView.setText(mSetting.getString(APP_PREFERENCES_TEMPERATURE,"Температура на улице 36°"));
        }
        if(mSetting.contains(APP_PREFERENCES_DATE)) {
            TextView textView = (TextView) findViewById(R.id.date);
            textView.setText(mSetting.getString(APP_PREFERENCES_DATE,DATE));
        }
        if(mSetting.contains(APP_PREFERENCES_PRESSURE_INFO)) {
            TextView textView = (TextView) findViewById(R.id.pressure);
            textView.setText(mSetting.getString(APP_PREFERENCES_PRESSURE_INFO,"Давление 759.00 мм. "));
        }
        if(mSetting.contains(APP_PREFERENCES_WIND_SPEED_INFO)) {
            TextView textView = (TextView) findViewById(R.id.windSpeed);
            textView.setText(mSetting.getString(APP_PREFERENCES_WIND_SPEED_INFO,"Скорость ветра 2 м.с "));
        }
//        boolean checked1 = getIntent().getBooleanExtra("WS",false);
//        findViewById(R.id.pressure).setVisibility(checked1 ? View.GONE : View.VISIBLE);
//
//        boolean checked2 = getIntent().getBooleanExtra("PS",false);
//        findViewById(R.id.windSpeed).setVisibility(checked2 ? View.GONE : View.VISIBLE);
    }

    public void city() {
        if(mSetting.contains(APP_PREFERENCES_CITY)){
            TextView textView = (TextView)findViewById(R.id.city);
            textView.setText(mSetting.getString(APP_PREFERENCES_CITY, "  "));
        }
//        try {
//            String city = getIntent().getExtras().getString("CITY");
//            TextView textView = findViewById(R.id.city);
//            textView.setText(city);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
    }

    public void infoCity(View view) {
        String city = null;
        if(mSetting.contains(APP_PREFERENCES_CITY)) {
            city = (mSetting.getString(APP_PREFERENCES_CITY, "  "));
        }
        String url = "https://www.ya.ru/" + city;
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void iconAbout(View view) {
        Toast.makeText(this, "http://www.freepik.com / Designed by Dimas_sugih / Freepik", Toast.LENGTH_LONG).show();
    }
}