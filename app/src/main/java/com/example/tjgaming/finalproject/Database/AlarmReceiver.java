package com.example.tjgaming.finalproject.Database;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.tjgaming.finalproject.R;
import com.example.tjgaming.finalproject.View.Authentication.LoginActivity;

import static com.example.tjgaming.finalproject.Database.Database.DAILY_REMINDER_REQUEST_CODE;

public class AlarmReceiver extends BroadcastReceiver {
    String idChannel = "finalproject";
    String TAG = "AlarmReceiver.class";
    String sName;
    int[] sTime;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: intent begins");
        Intent notificationIntent = new Intent(context, LoginActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        sName = intent.getStringExtra("Show Name");
        sTime = intent.getIntArrayExtra("Show Times");

        String minutes = "";
        String hour = "";
        String amOrPm = "";

        if (sTime[0] > 12){
            hour = String.valueOf(sTime[0] - 12);
            amOrPm = "PM";
        } else {
            hour = String.valueOf(sTime[0]);
            amOrPm = "AM";
        }

        if (sTime[1] < 10) {
            minutes = sTime[1] + "0";
        } else {
            minutes = String.valueOf(sTime[1]);
        }

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(LoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(DAILY_REMINDER_REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"default");
        builder .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle("MediaHub Alerts")
                .setContentText(sName +" will air at " +hour+":"+minutes+". " +amOrPm+ " Don't miss it! ")
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pendingIntent).build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(idChannel);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(idChannel, "MediaHub", NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);

            }
        }
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, builder.build());
        Log.d(TAG, "onReceive: show notification");

    }
}
