package com.hw.weather;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hw.weather.broadcast.NetworkAlerts;
import com.hw.weather.broadcast.PowerAlerts;
import com.hw.weather.fragment.main.MainFragment;
import com.hw.weather.fragment.maps.MapsFragment;
import com.hw.weather.fragment.search.SearchFragment;
import com.hw.weather.fragment.setting.MySettingFragment;
import com.hw.weather.fragment.weatherRequest.MainWeather;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity implements Constants, SupportItemSelect {

    private NetworkAlerts networkAlerts = new NetworkAlerts();
    private PowerAlerts powerAlerts = new PowerAlerts();
    private CoordinatorLayout coordinatorLayout;
    private OpenWeatherByName openWeatherByName;
    private String text;
    private NavigationView navigationView;
    private SharedPreferences mSetting;

    // Используется, чтобы определить результат Activity регистрации через
    // Google
    private static final int RC_SIGN_IN = 40404;
    private static final String TAG = "GoogleAuth";

    // Клиент для регистрации пользователя через Google
    private GoogleSignInClient googleSignInClient;

    // Кнопка регистрации через Google
    private com.google.android.gms.common.SignInButton buttonSignIn;


    private void initNotificationChannel() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel("3", "channel", importance);
            notificationManager.createNotificationChannel(channel);
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w(TAG, "getInstanceId failed", task.getException());
                return;
            }

            // Get Instance ID token
            String token = task.getResult().getToken();
            Log.d(TAG, token);
        });
    }

    private void registerBroadcastReceivers() {
        registerReceiver(networkAlerts, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        registerReceiver(powerAlerts, new IntentFilter(Intent.ACTION_BATTERY_LOW));
    }

    private void initRetrofit() {
        Retrofit retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        openWeatherByName = retrofit.create(OpenWeatherByName.class);
    }

    private void requestRetrofit(String cityCountry, String keyApi) {
        openWeatherByName.loadWeather(cityCountry, keyApi)
                .enqueue(new Callback<MainWeather>() {
                    @Override
                    public void onResponse(Call<MainWeather> call, Response<MainWeather> response) {
                        if (response.body() != null) {
                            String temp = String.valueOf(response.body().getMain().getTemp() + absoluteZero).substring(0,2) + "°C";
                            Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MainWeather> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startFragment(new MainFragment());

        } else if (id == R.id.nav_settings) {
            startFragment(new MySettingFragment());

        } else if (id == R.id.nav_search) {
            startFragment(new SearchFragment());

        } else if (id == R.id.nav_map) {
            startFragment(new MapsFragment());

        } else if (id == R.id.nav_feedback) {
            Snackbar.make(findViewById(R.id.drawer_layout), "Feedback", Snackbar.LENGTH_LONG).setAction(" -> ", view -> {
                String url = "https://MrAlex@gmail.com";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }).show();

        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "Developed by MrAlex", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkAlerts);
        unregisterReceiver(powerAlerts);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Здесь определяем меню приложения (активити)
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.action_search); // поиск пункта меню поиска
        final SearchView searchText = (SearchView) search.getActionView(); // строка поиска
        searchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // реагирует на конец ввода поиска
            @Override
            public boolean onQueryTextSubmit(String query) {
                text = query;
                requestRetrofit(query, WEATHER_API_KEY);
                Snackbar.make(searchText, query, Snackbar.LENGTH_LONG).show();
                searchText.clearFocus();
                return true;
            }

            // реагирует на нажатие каждой клавиши
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Проверим, входил ли пользователь в это приложение через Google
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            // Пользователь уже входил, сделаем кнопку недоступной
            buttonSignIn.setEnabled(false);
            buttonSignIn.setVisibility(View.INVISIBLE);
            // Обновим почтовый адрес этого пользователя и выведем его на экран
            updateEmail(account.getEmail());
            updateName(account.getDisplayName());
            if (account.getPhotoUrl() != null) {
                updatePhoto(account.getPhotoUrl());
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = initToolbar();
        initToolbar();
        initDrawer(toolbar);
        initRetrofit();
        registerBroadcastReceivers();
        initNotificationChannel();
        startFragment(new MainFragment());
        // Конфигурация запроса на регистрацию пользователя, чтобы получить
        // идентификатор пользователя, его почту и основной профайл
        // (регулируется параметром)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Получаем клиента для регистрации и данные по клиенту
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Кнопка регистрации пользователя
        buttonSignIn = navigationView.getHeaderView(0).findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                signIn();
                                            }
                                        }
        );

    }

    // Получаем результаты аутентификации от окна регистрации пользователя
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // Когда сюда возвращается Task, результаты аутентификации уже
            // готовы
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Инициируем регистрацию пользователя
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Получаем данные пользователя
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Регистрация прошла успешно
            buttonSignIn.setEnabled(false);
            buttonSignIn.setVisibility(View.INVISIBLE);
            updateEmail(account.getEmail());
            updateName(account.getDisplayName());
            if (account.getPhotoUrl() != null) {
                updatePhoto(account.getPhotoUrl());
            }
            Log.i(TAG, account.getEmail() + account.getPhotoUrl() + account.getDisplayName());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure
            // reason. Please refer to the GoogleSignInStatusCodes class
            // reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void updatePhoto(Uri photoUrl) {
        ImageView photo = navigationView.getHeaderView(0).findViewById(R.id.google_photo);
        Picasso.get()
                .load(photoUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        File directory = getDir("photo", Context.MODE_PRIVATE);
                        File file = new File(directory, "photo.png");
                        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
//                            Matrix matrix = new Matrix();
//                            // RESIZE THE BIT MAP
//                            matrix.postScale(75, 75);
//                            // "RECREATE" THE NEW BITMAP
//                            Bitmap BM = Bitmap.createBitmap(
//                                    bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                            Bitmap bm = Bitmap.createScaledBitmap(
                                    bitmap, 125, 125, false);
                            bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            photo.setImageBitmap(bm);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String path = directory.getAbsolutePath();
                        mSetting = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = mSetting.edit();
                        editor.putString(APP_PREFERENCES_PHOTO, path + "\\photo.png").apply();
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        Log.w(TAG, e);
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
    }

    private void updateName(String displayName) {
        TextView token = navigationView.getHeaderView(0).findViewById(R.id.second_text);
        token.setText(displayName);
    }

    // Обновляем данные о пользователе на экране
    private void updateEmail(String idToken) {
        TextView token = navigationView.getHeaderView(0).findViewById(R.id.first_text);
        token.setText(idToken);
    }


    @Override
    public void startFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.test_replace, fragment);
        ft.commit();
    }

}