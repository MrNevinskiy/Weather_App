package com.hw.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import static androidx.appcompat.app.AppCompatDelegate.*;

public class MySettingsActivity extends AppCompatActivity {

    private final String onCreate = "В данный момент onCreate";
    private final String onStart = "В данный момент onStart";
    private final String onResume = "В данный момент onResume";
    private final String onPause = "В данный момент onResume";
    private final String onStop = "В данный момент onStop";
    private final String onDestroy = "В данный момент onDestroy";

    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toast.makeText(this, onCreate, Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate);
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

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад",Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void nightActivity(View view) {
        // TODO: 01.07.2020  
    }

    public void windSpeed(View view) {
        // Выкидывает из приложения
        boolean checked = ((CheckBox) view).isChecked();
        mainActivity.findViewById(R.id.windSpeed).setVisibility(checked ? View.GONE : View.VISIBLE);
    }

    public void pressure(View view) {
        // Выкидывает из приложения
        boolean checked = ((CheckBox) view).isChecked();
        mainActivity.findViewById(R.id.pressure).setVisibility(checked ? View.GONE : View.VISIBLE);
    }
}