package com.hw.weather.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

import androidx.core.app.NotificationCompat;

import com.hw.weather.R;

public class NetworkAlerts extends BroadcastReceiver {

    private int Id = 100;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "3")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.connectivity_change));

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities network = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());

        if (network != null) {
            if (network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                builder.setContentText(context.getString(R.string.сonnection_restored));
            }
            if (!network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                builder.setContentText(context.getString(R.string.cellular_network));
            }
            if (network.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) && !network.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                builder.setContentText(context.getString(R.string.wifi_network));
            }
        } else {
            builder.setContentText(context.getString(R.string.no_network));
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(Id++, builder.build());
    }
}
