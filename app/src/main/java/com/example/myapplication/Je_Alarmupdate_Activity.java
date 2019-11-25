package com.example.myapplication;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

public class Je_Alarmupdate_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    TimePicker timePicker;
    CheckBox mon, tue, wed, thu, fri, sat, sun;
    Button btn;
    String ID, hour, minute, a, b, c, d, e, f, g;
    Integer h, m, monnum, tuenum, wednum, thunum, frinum, satnum, sunnum;
    LinearLayout linear;
    EditText body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_alarmupdate_activity);
        Intent intent = getIntent();
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        ID = intent.getStringExtra("ID");
        hour = intent.getStringExtra("hour");
        minute = intent.getStringExtra("minute");
        a = intent.getStringExtra("a");
        b = intent.getStringExtra("b");
        c = intent.getStringExtra("c");
        d = intent.getStringExtra("d");
        e = intent.getStringExtra("e");
        f = intent.getStringExtra("f");
        g = intent.getStringExtra("g");
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

        if (a.equals("1")) mon.setChecked(true);
        if (b.equals("1")) tue.setChecked(true);
        if (c.equals("1")) wed.setChecked(true);
        if (d.equals("1")) thu.setChecked(true);
        if (e.equals("1")) fri.setChecked(true);
        if (f.equals("1")) sat.setChecked(true);
        if (g.equals("1")) sun.setChecked(true);

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

        timePicker.setCurrentHour(Integer.parseInt(hour));
        timePicker.setCurrentMinute(Integer.parseInt(minute));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    sqlDB.execSQL("UPDATE alarm SET hour = '" + h.toString() + "', minute = '" + m.toString() + "', mon = '" + monnum.toString() + "', tue = '" + tuenum.toString() + "', wed = '" + wednum.toString() + "', thu = '" + thunum.toString() + "', fri = '" + frinum.toString() + "', sat = '" + satnum.toString() + "', sun = '" + sunnum.toString() + "', body = '" +body.getText().toString()+ "' WHERE _id = '" + ID + "'AND hour = '" + hour + "' AND minute = '" + minute + "'AND mon = '" + a + "'AND tue = '" + b + "'AND wed = '" + c + "'AND thu = '" + d + "'AND fri = '" + e + "'AND sat = '" + f + "'AND sun = '" + g + "'");
                else
                    sqlDB.execSQL("UPDATE alarm SET hour = '" + h.toString() + "', minute = '" + m.toString() + "', mon = '" + monnum.toString() + "', tue = '" + tuenum.toString() + "', wed = '" + wednum.toString() + "', thu = '" + thunum.toString() + "', fri = '" + frinum.toString() + "', sat = '" + satnum.toString() + "', sun = '" + sunnum.toString() + "', body = '알람 시간입니다' WHERE _id = '" + ID + "'AND hour = '" + hour + "' AND minute = '" + minute + "'AND mon = '" + a + "'AND tue = '" + b + "'AND wed = '" + c + "'AND thu = '" + d + "'AND fri = '" + e + "'AND sat = '" + f + "'AND sun = '" + g + "'");
                sqlDB.close();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.deleteNotificationChannel("Healing Mind");
                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
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
                    Je_Alarmset_Activity.TFLAG = true;
                    Intent backStartIntent = new Intent(Je_Alarmupdate_Activity.this, MyService.class);
                    startForegroundService(backStartIntent);
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