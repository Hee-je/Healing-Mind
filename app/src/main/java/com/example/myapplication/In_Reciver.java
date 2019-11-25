package com.example.myapplication;

import android.app.NotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;


public class In_Reciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {

                Intent i = new Intent(context, MyService.class);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(i);
                }
                Log.d("BootReceiver", "Service loaded at start..");


            }
        }
    }
}
