package com.hw.weather.broadcast;

import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hw.weather.Constants;
import com.hw.weather.R;

public class FirebaseService extends FirebaseMessagingService implements Constants {

    private int ID = 0;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG, remoteMessage.getNotification().getBody());
        String title = remoteMessage.getNotification().getTitle();
        if (title == null) {
            title = "Push Message";
        }
        String text = remoteMessage.getNotification().getBody();
        createNotification(title, text);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d(TAG, "Token:" + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // TODO: 14.08.2020
    }

    private void createNotification(String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "3")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(ID++, builder.build());
    }
}
