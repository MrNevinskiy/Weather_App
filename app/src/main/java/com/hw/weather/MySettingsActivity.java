package com.hw.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;


public class MySettingsActivity extends AppCompatActivity implements Constants {

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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("ws",((CheckBox)findViewById(R.id.windSpeed)).isChecked());
        outState.putBoolean("ps",((CheckBox)findViewById(R.id.pressure)).isChecked());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getBoolean("ws", true);
        savedInstanceState.getBoolean("ps", true);
    }

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад", Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void windSpeed(View view) {
        boolean checked1 = ((CheckBox) view).isChecked();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("WS", checked1);
        Log.d(String.valueOf(this), "windSpeed");
//        startActivity(intent);
    }

    public void pressure(View view) {
        boolean checked2 = ((CheckBox) view).isChecked();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("PS", checked2);
        Log.d(String.valueOf(this), "pressure");
        //startActivity(intent);
    }

    public void nightActivity(View view) {
        // TODO: 01.07.2020
    }

    public void saveOnClick(View view) {

        // TODO: 03.07.2020
        // передача в один объект двух интентов и далее их разбор в главном окне
    }
}