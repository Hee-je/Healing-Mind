package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Locale;

public class MyService extends Service {
    private int a = 0;
    int[][] num = new int[9][50];
    String[] body = new String[50];
    public static boolean Start = false;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    public boolean F;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("Healing Mind", "Healing Mind", NotificationManager.IMPORTANCE_DEFAULT);
            {
                notificationChannel.setDescription("Healing Mind");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        Log.i("onStartCommand-Action1", "---서비스 스타트--- ");

        final Intent notificationIntent = new Intent(this, In_Frame_Activity.class);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.rest);
        final Notification notification = new NotificationCompat.Builder(this, "Healing Mind")
                .setContentTitle("Healing Mind")
                .setSmallIcon(R.mipmap.rest)
                .setLargeIcon(icon)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MIN)
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .build();
        startForeground(1, notification);
        F=true;

        myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        Thread bt = new Thread(new Runnable() {
            @Override
            public void run() {
                while (F) {
                    int i = 0;
                    Calendar c = Calendar.getInstance();
                    int test = 0;
                    Cursor cursor = sqlDB.rawQuery("SELECT *FROM alarm;", null);
                    while (cursor.moveToNext()) {
                        test = 1;
                        body[i]=cursor.getString(11);
                        num[0][i] = Integer.parseInt(cursor.getString(2));
                        num[1][i] = Integer.parseInt(cursor.getString(3));
                        num[2][i] = Integer.parseInt(cursor.getString(4));
                        num[3][i] = Integer.parseInt(cursor.getString(5));
                        num[4][i] = Integer.parseInt(cursor.getString(6));
                        num[5][i] = Integer.parseInt(cursor.getString(7));
                        num[6][i] = Integer.parseInt(cursor.getString(8));
                        num[7][i] = Integer.parseInt(cursor.getString(9));
                        num[8][i] = Integer.parseInt(cursor.getString(10));
                        Log.i("알람 시작", num[0][i] + "시" + num[1][i] + "분으로 알람 시작\n");
                        i++;
                    }
                    if (test == 0) {
                        Je_Alarmset_Activity.TFLAG = false;
                        F=false;
                    }
                    i--;
                    while (i != -1) {
                        Log.i("알람 비교", "알람 울리는 시간인가?\n" + num[0][i] + c.get(Calendar.HOUR) + num[1][i] + c.get(Calendar.MINUTE));
                        if (num[0][i] == c.get(Calendar.HOUR_OF_DAY) && num[1][i] == c.get(Calendar.MINUTE)) {
                            Log.i("알람 1단계", "알람 알람\n");

                            if (c.get(Calendar.DAY_OF_WEEK) == 1) {
                                Log.i("일요일알람", "\n");
                                if (num[c.get(Calendar.DAY_OF_WEEK) + 7][i] == 1) {
                                    Log.i("알람 울림", "알람 울리는시간임\n");
                                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Healing Mind")
                                            .setLargeIcon(null).setSmallIcon(R.mipmap.rest)
                                            .setWhen(System.currentTimeMillis()).setShowWhen(true)
                                            .setContentIntent(pendingIntent)
                                            .setPriority(Notification.PRIORITY_MIN)
                                            .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                                            .setContentTitle(body[i]);
                                    PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                                    PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                                            PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                            PowerManager.ON_AFTER_RELEASE, "My:Tag");
                                    wakeLock.acquire(6000);
                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0, builder.build());
                                }
                            } else if (num[c.get(Calendar.DAY_OF_WEEK)][i] == 1) {
                                final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Healing Mind")
                                        .setLargeIcon(null).setSmallIcon(R.mipmap.rest)
                                        .setWhen(System.currentTimeMillis()).setShowWhen(true)
                                        .setContentIntent(pendingIntent)
                                        .setPriority(Notification.PRIORITY_MIN)
                                        .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                                        .setContentTitle(body[i]);
                                PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                                        PowerManager.ON_AFTER_RELEASE, "My:Tag");
                                wakeLock.acquire(5000);
                                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.notify(0, builder.build());
                            }
                        }
                        i--;
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!F) {
                        Log.i("background-counter", "---백스레드 중지---");
                        onDestroy();
                        break;
                    }
                }
            }
        });
        bt.setName("백그라운드스레드");
        Start = true;
        bt.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Start = false;
        Je_Alarmset_Activity.TFLAG = false;
        F=false;
    }
}