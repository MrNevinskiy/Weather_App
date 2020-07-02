package com.hw.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private final String onCreate = "В данный момент onCreate";
    private final String onStart = "В данный момент onStart";
    private final String onResume = "В данный момент onResume";
    private final String onPause = "В данный момент onResume";
    private final String onStop = "В данный момент onStop";
    private final String onDestroy = "В данный момент onDestroy";

    Singleton singleton;

    public String test() throws NullPointerException{
        EditText editText = (EditText)findViewById(R.id.textView);
        return editText.getText().toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this, onCreate,Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onCreate);
        setContentView(R.layout.activity_search);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, onStart,Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, onResume,Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onResume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, onPause,Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onPause);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, onStop,Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStop);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, onDestroy,Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onDestroy);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("city", test());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        savedInstanceState.getString("city");
    }

    public void toMainActivity(View view) {
        Toast.makeText(this, "Назад",Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), "toMainActivity");
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void searchCity(View view) {
        Toast.makeText(this, "Тут будет поиск города",Toast.LENGTH_SHORT).show();
        Log.d(String.valueOf(this), onStart);
    }
}