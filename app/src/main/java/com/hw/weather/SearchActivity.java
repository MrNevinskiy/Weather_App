package com.hw.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

public class SearchActivity extends AppCompatActivity implements Constants {

    SharedPreferences mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toast.makeText(this, onCreate, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate);
        mSetting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSetting.contains(APP_PREFERENCES_CITY)){
            EditText editText = (EditText) findViewById(R.id.entryCity);
            editText.setText(mSetting.getString(APP_PREFERENCES_CITY, " "));
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
        infoCityTest(); //Заплатка для дальнейшей разработки
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

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void infoCityTest(){
        Date date = new Date();
        String DATE = date.toString();
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_TEMPERATURE,"Температура на улице 36°");
        editor.putString(APP_PREFERENCES_DATE,DATE);
        editor.putString(APP_PREFERENCES_UPDATE,DATE);
        editor.putString(APP_PREFERENCES_PRESSURE_INFO,"Давление 759.00 мм. ");
        editor.putString(APP_PREFERENCES_WIND_SPEED_INFO,"Скорость ветра 2 м.с ");
        editor.apply();
    }

    public void searchCity(View view) {
        EditText searchCity = (EditText)findViewById(R.id.entryCity);
        String city = searchCity.getText().toString();
        SharedPreferences.Editor editor = mSetting.edit();
        editor.putString(APP_PREFERENCES_CITY,city);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //*****************************************************************//
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("CITY", city);
//        Toast.makeText(this, "Город: " + city, Toast.LENGTH_LONG).show();
//        Log.d(String.valueOf(this), onStart);
//        startActivity(intent);
        //*****************************************************************//
        // игрался с Singleton
//        EditText searchCity = (EditText)findViewById(R.id.entryCity);
//        String city = searchCity.getText().toString();
//        Singleton singleton = Singleton.getInstance(city);
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra("CITY", singleton.value);
//        Toast.makeText(this, "Город: " + singleton.value, Toast.LENGTH_LONG).show();
//        Log.d(String.valueOf(this), onStart);
//        startActivity(intent);
        //*****************************************************************//
    }
}