package com.onevest.dev.tulung;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.NotificationCompat;

import com.onevest.dev.tulung.main.activity.MainActivity;

/**
 * Created by Shoddiq Jati Premono on 03/08/2017.
 */

public class NotificationHelper {

    private Context context;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    public void sendNotification(String msg) {
        long now = System.currentTimeMillis();
        Intent notificationIntent = new Intent(context.getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent notifPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        int largeIcon;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            largeIcon = R.mipmap.ic_launcher_round;
        } else {
            largeIcon = R.mipmap.ic_launcher;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
                    .setColor(context.getColor(R.color.colorAccent))
                    .setContentText(msg)
                    .setContentTitle("TULUNG")
                    .setContentIntent(notifPendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(now);
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
                    .setContentText(msg)
                    .setContentTitle("TULUNG")
                    .setContentIntent(notifPendingIntent)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setWhen(now);
        }

        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setGroup(context.getString(R.string.app_name)).setGroupSummary(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) now, builder.build());
    }
}
