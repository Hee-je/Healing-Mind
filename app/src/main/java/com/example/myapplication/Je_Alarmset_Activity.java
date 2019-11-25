package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class Je_Alarmset_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    Calendar time = Calendar.getInstance();
    TimePicker timePicker;
    public static String ID;
    CheckBox mon, tue, wed, thu, fri, sat, sun;
    Button btn;
    Integer h, m, monnum, tuenum, wednum, thunum, frinum, satnum, sunnum;
    LinearLayout linear;
    EditText body;
    public static boolean TFLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_alarmset_activity);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        myHelper = new MyDBHelper(this);
        timePicker = findViewById(R.id.time);
        mon = findViewById(R.id.mon);
        tue = findViewById(R.id.tue);
        wed = findViewById(R.id.wed);
        thu = findViewById(R.id.thu);
        fri = findViewById(R.id.fri);
        sat = findViewById(R.id.sat);
        sun = findViewById(R.id.sun);
        btn = findViewById(R.id.btn);
        body = findViewById(R.id.body);
        mon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mon.isChecked()) {
                    mon.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.mon_yes)
                    );
                } else {
                    mon.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.mon_no)
                    );
                }
            }
        });

        tue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tue.isChecked()) {
                    tue.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.tue_yes)
                    );
                } else {
                    tue.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.tue_no)
                    );
                }
            }
        });

        wed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wed.isChecked()) {
                    wed.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.wen_yes)
                    );
                } else {
                    wed.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.wen_no)
                    );
                }
            }
        });

        thu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (thu.isChecked()) {
                    thu.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.thu_yes)
                    );
                } else {
                    thu.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.thu_no)
                    );
                }
            }
        });

        fri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fri.isChecked()) {
                    fri.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.fri_yes)
                    );
                } else {
                    fri.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.fri_no)
                    );
                }
            }
        });

        sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sat.isChecked()) {
                    sat.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.sat_yes)
                    );
                } else {
                    sat.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.sat_no)
                    );
                }
            }
        });

        sun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sun.isChecked()) {
                    sun.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.sun_yes)
                    );
                } else {
                    sun.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.sun_no)
                    );
                }
            }
        });

        int week = time.get(Calendar.DAY_OF_WEEK);
        switch (week) {
            case 1:
                sun.setChecked(true);
                break;
            case 2:
                mon.setChecked(true);
                break;
            case 3:
                tue.setChecked(true);
                break;
            case 4:
                wed.setChecked(true);
                break;
            case 5:
                thu.setChecked(true);
                break;
            case 6:
                fri.setChecked(true);
                break;
            case 7:
                sat.setChecked(true);
                break;
        }

        if (mon.isChecked()) {
            mon.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.mon_yes)
            );
        } else {
            mon.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.mon_no)
            );
        }

        if (tue.isChecked()) {
            tue.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.tue_yes)
            );
        } else {
            tue.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.tue_no)
            );
        }

        if (wed.isChecked()) {
            wed.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.wen_yes)
            );
        } else {
            wed.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.wen_no)
            );
        }

        if (thu.isChecked()) {
            thu.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.thu_yes)
            );
        } else {
            thu.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.thu_no)
            );
        }

        if (fri.isChecked()) {
            fri.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.fri_yes)
            );
        } else {
            fri.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.fri_no)
            );
        }

        if (sat.isChecked()) {
            sat.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.sat_yes)
            );
        } else {
            sat.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.sat_no)
            );
        }

        if (sun.isChecked()) {
            sun.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.sun_yes)
            );
        } else {
            sun.setBackgroundDrawable(
                    getResources().
                            getDrawable(R.drawable.sun_no)
            );
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mon.isChecked() || tue.isChecked() || wed.isChecked() || thu.isChecked() || fri.isChecked() || sat.isChecked() || sun.isChecked()) {
                    h = timePicker.getCurrentHour();
                    m = timePicker.getCurrentMinute();
                    if (mon.isChecked()) monnum = 1;
                    else monnum = 0;
                    if (tue.isChecked()) tuenum = 1;
                    else tuenum = 0;
                    if (wed.isChecked()) wednum = 1;
                    else wednum = 0;
                    if (thu.isChecked()) thunum = 1;
                    else thunum = 0;
                    if (fri.isChecked()) frinum = 1;
                    else frinum = 0;
                    if (sat.isChecked()) satnum = 1;
                    else satnum = 0;
                    if (sun.isChecked()) sunnum = 1;
                    else sunnum = 0;
                    sqlDB = myHelper.getWritableDatabase();
                    if (!body.getText().toString().equals(null))
                        sqlDB.execSQL("INSERT INTO alarm VALUES (NULL, '" + ID + "','" + h.toString() + "','" + m.toString() + "','" + monnum.toString() + "','" + tuenum.toString() + "','" + wednum.toString() + "','" + thunum.toString() + "','" + frinum.toString() + "','" + satnum.toString() + "','" + sunnum.toString() + "','" + body.getText().toString() + "');");
                    else
                        sqlDB.execSQL("INSERT INTO alarm VALUES (NULL, '" + ID + "','" + h.toString() + "','" + m.toString() + "','" + monnum.toString() + "','" + tuenum.toString() + "','" + wednum.toString() + "','" + thunum.toString() + "','" + frinum.toString() + "','" + satnum.toString() + "','" + sunnum.toString() + "','명상 시간입니다');");
                    sqlDB.close();
                } else {
                    Toast.makeText(Je_Alarmset_Activity.this, "요일을 선택해주세요", Toast.LENGTH_SHORT).show();
                }
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
                    Toast.makeText(Je_Alarmset_Activity.this, "안드로이드 버전 업데이트가 필요합니다.", Toast.LENGTH_SHORT).show();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    TFLAG = false;
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
                        TFLAG = true;
                        if (!MyService.Start) {
                            Intent backStartIntent = new Intent(Je_Alarmset_Activity.this, MyService.class);
                            startForegroundService(backStartIntent);
                        }
                    }
                }
                Intent intent = new Intent(getApplicationContext(), Je_Alarm_Activity.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Je_Alarm_Activity.class);
        intent.putExtra("ID", ID);
        startActivity(intent);
    }
}