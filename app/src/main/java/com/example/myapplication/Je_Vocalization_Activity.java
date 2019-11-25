package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Je_Vocalization_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    private Context mContext;
    TextView[] numTesx = new TextView[6];
    TextView title_step1, title_step2, title_step3, title_step4, title_step5, title_step6;
    DatePicker dday;
    Integer num;
    LinearLayout linear;
    Integer[] numTextIds = {R.id.step1, R.id.step2, R.id.step3, R.id.step4, R.id.step5, R.id.step6};
    String ID, letter, contents, IP, step, VIDEO_URL, TAG = "phpquerytest", IP_Contents = "";
    int e1 = 0, e2 = 0;
    ProgressBar progress;
    boolean check = true;
    VideoView videoView;
    ImageView img;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_voclaization_tool);
        TextView caution = (TextView) findViewById(R.id.caution);
        linear = (LinearLayout) findViewById(R.id.linear);
        TextView title = (TextView) findViewById(R.id.title);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        letter = intent.getStringExtra("letter");
        VIDEO_URL = "http://" + IP_Contents + "/mp3/";
        title.setText("한글 발성 명상 (" + letter + ")");
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        NetworkInfo mNetworkState = getNetworkInfo();
        if (mNetworkState != null && mNetworkState.isConnected()) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Je_Vocalization_Activity.this);
            builder.setCancelable(false);
            builder.setTitle("인터넷 연결을 확인해 주세요");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
        }
        title_step1 = (TextView) findViewById(R.id.title_step1);
        title_step2 = (TextView) findViewById(R.id.title_step2);
        title_step3 = (TextView) findViewById(R.id.title_step3);
        title_step4 = (TextView) findViewById(R.id.title_step4);
        title_step5 = (TextView) findViewById(R.id.title_step5);
        title_step6 = (TextView) findViewById(R.id.title_step6);
        mContext = getApplicationContext();
        getHashKey(mContext);
        dday = (DatePicker) findViewById(R.id.dpick);
        progress = (ProgressBar) findViewById(R.id.progress);
        videoView = (VideoView) findViewById(R.id.fullvideo);
        img = (ImageView) findViewById(R.id.img);
        myHelper = new MyDBHelper(this);
        Cursor cursor;
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM vocalization WHERE _id = '" + ID + "' AND letter ='" + letter + "';", null);
        if (!cursor.moveToNext()) {
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO vocalization VALUES (null,'" + ID + "','" + letter + "','" + "0" + "','" + "0" + "','" + "0" + "','" + "0" + "','" + "0" + "','" + "0" + "');");
        }
        cursor.close();
        sqlDB.close();
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM vocalization WHERE _id = '" + ID + "' AND letter ='" + letter + "';", null);
        cursor.moveToNext();
        if (cursor.getString(3).equals("1")) {
            title_step1.setPaintFlags(title_step1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (cursor.getString(4).equals("1")) {
            title_step2.setPaintFlags(title_step2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (cursor.getString(5).equals("1")) {
            title_step3.setPaintFlags(title_step3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (cursor.getString(6).equals("1")) {
            title_step4.setPaintFlags(title_step4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (cursor.getString(7).equals("1")) {
            title_step5.setPaintFlags(title_step5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        if (cursor.getString(8).equals("1")) {
            title_step6.setPaintFlags(title_step6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
        num = Integer.parseInt(cursor.getString(3)) + Integer.parseInt(cursor.getString(4)) + Integer.parseInt(cursor.getString(5)) + Integer.parseInt(cursor.getString(6)) + Integer.parseInt(cursor.getString(7)) + Integer.parseInt(cursor.getString(8));
        progress.setProgress(100 * num);
        cursor.close();
        sqlDB.close();
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "발성" + "';", null);
        if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
            img.setImageResource(R.mipmap.heart_1);
        } else { // 즐겨찾기를 했을때
            img.setImageResource(R.mipmap.heart_2);
        }
        cursor.close();
        sqlDB.close();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;
                sqlDB = myHelper.getReadableDatabase();
                cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "발성" + "';", null);
                if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO bookmark VALUES (NULL, '" + ID + "','" + "mipmap/two" + "','" + "발성" + "','" + letter + "');");
                    img.setImageResource(R.mipmap.heart_2);
                } else { // 즐겨찾기를 했을때
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("DELETE FROM bookmark WHERE _id =  '" + ID + "' AND title = '" + "발성" + "'AND letter = '" + letter + "'");
                    img.setImageResource(R.mipmap.heart_1);
                }
                cursor.close();
                sqlDB.close();
            }
        });
        caution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Je_Vocalization_Activity.this);
                builder.setTitle("주의사항");
                builder.setMessage("※ 등과 허리를 바료게 펴고 앉는다.\n※ 발성이 끝난 후 숨을 들이마신 뒤, 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                builder.show();
            }
        });
        MediaController controller = new MediaController(Je_Vocalization_Activity.this);
        videoView.setMediaController(controller);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Integer y = dday.getYear();
                Integer mon = dday.getMonth() + 1;
                Integer mon2 = dday.getMonth();
                Integer day = dday.getDayOfMonth();
                Calendar c = Calendar.getInstance();
                String dates = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                long time = System.currentTimeMillis();
                SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                String str = dayTime.format(new Date(time));
                InsertData_con task_con = new InsertData_con();
                task_con.execute("http://" + IP + "/contents.php", ID, "발성", step, letter, dates, str);
                String calendar_date = "CalendarDay" + "{" + y.toString() + "-" + mon2.toString() + "-" + day.toString() + "}";
                String schedule = letter + " - " + step;
                Cursor cursor;
                sqlDB = myHelper.getReadableDatabase();
                cursor = sqlDB.rawQuery("SELECT *FROM vocalization WHERE _id = '" + ID + "' AND letter = '" + letter + "';", null);
                cursor.moveToNext();
                if (step.equals("STEP 1")) {
                    if (cursor.getString(3).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step1 = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                } else if (step.equals("STEP 2")) {
                    if (cursor.getString(4).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step2 = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                } else if (step.equals("STEP 3")) {
                    if (cursor.getString(5).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step3 = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                } else if (step.equals("STEP 4")) {
                    if (cursor.getString(6).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step4 = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                } else if (step.equals("STEP 5")) {
                    if (cursor.getString(7).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step5 = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                } else if (step.equals("STEP 6")) {
                    if (cursor.getString(8).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step6 = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                } else if (step.equals("ALL")) {
                    if (cursor.getString(3).equals("0") || cursor.getString(4).equals("0") || cursor.getString(5).equals("0") || cursor.getString(6).equals("0") || cursor.getString(7).equals("0") || cursor.getString(8).equals("0")) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("UPDATE vocalization SET step1 = '" + "1" + "', step2 = '" + "1" + "', step3 = '" + "1" + "', step4 = '" + "1" + "', step5 = '" + "1" + "', step6 = '" + "1" + "' WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                    }
                }
                Cursor cursor2;
                sqlDB = myHelper.getReadableDatabase();
                cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "'AND schedule = '" + schedule + "';", null);
                if (!cursor2.moveToNext()) {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + schedule + "');");
                }
                cursor.close();
                sqlDB.close();
                Cursor attendance_cursor;
                sqlDB = myHelper.getReadableDatabase();
                attendance_cursor = sqlDB.rawQuery("SELECT *FROM attendance WHERE _id = '" + ID + "' AND year = '" + y.toString() + "'AND month ='" + mon.toString() + "'AND day ='" + day.toString() + "';", null);
                if (!attendance_cursor.moveToNext()) {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO attendance VALUES (null,'" + ID + "','" + y.toString() + "','" + mon.toString() + "','" + day.toString() + "');");
                }
                attendance_cursor.close();
                sqlDB.close();
                sqlDB = myHelper.getReadableDatabase();
                cursor = sqlDB.rawQuery("SELECT *FROM vocalization WHERE _id = '" + ID + "' AND letter = '" + letter + "';", null);
                cursor.moveToNext();
                if (cursor.getString(3).equals("1")) {
                    title_step1.setPaintFlags(title_step1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                if (cursor.getString(4).equals("1")) {
                    title_step2.setPaintFlags(title_step2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                if (cursor.getString(5).equals("1")) {
                    title_step3.setPaintFlags(title_step3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                if (cursor.getString(6).equals("1")) {
                    title_step4.setPaintFlags(title_step4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                if (cursor.getString(7).equals("1")) {
                    title_step5.setPaintFlags(title_step5.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                if (cursor.getString(8).equals("1")) {
                    title_step6.setPaintFlags(title_step6.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                }
                num = Integer.parseInt(cursor.getString(3)) + Integer.parseInt(cursor.getString(4)) + Integer.parseInt(cursor.getString(5)) + Integer.parseInt(cursor.getString(6)) + Integer.parseInt(cursor.getString(7)) + Integer.parseInt(cursor.getString(8));
                progress.setProgress(100 * num);
                cursor.close();
                sqlDB.close();
                if (num == 6) {
                    Cursor recently_cursor;
                    sqlDB = myHelper.getReadableDatabase();
                    recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "발성" + "';", null);
                    if (!recently_cursor.moveToNext()) {
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/two" + "','" + "발성" + "','" + letter + "');");
                    }
                    recently_cursor.close();
                    sqlDB.close();
                }
                check = true;
            }
        });
        for (int i = 0; i < numTextIds.length; i++) {
            numTesx[i] = (TextView) findViewById(numTextIds[i]);
        }
        switch (letter) {
            case "ㄱ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 10;
                break;
            case "ㄴ":
                numTesx[0].setText("힘의 중심을 배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 9;
                e2 = 10;
                break;
            case "ㄷ":
                numTesx[0].setText("힘의 중심을 허리에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 10;
                break;
            case "ㄹ":
                numTesx[0].setText("힘의 중심을 배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 9;
                break;
            case "ㅁ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 10;
                break;
            case "ㅂ":
                numTesx[0].setText("힘의 중심을 엉치에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 10;
                break;
            case "ㅅ":
                numTesx[0].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 10;
                e2 = 9;
                break;
            case "ㅇ":
                numTesx[0].setText("힘의 중심을 배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을  내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 9;
                e2 = 10;
                break;
            case "ㅈ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 9;
                break;
            case "ㅊ":
                numTesx[0].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 10;
                e2 = 9;
                break;
            case "ㅋ":
                numTesx[0].setText("힘의 중심을 배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 9;
                break;
            case "ㅌ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 10;
                break;
            case "ㅍ":
                numTesx[0].setText("힘의 중심을 허리에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 10;
                e2 = 9;
                break;
            case "ㅎ":
                numTesx[0].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 10;
                e2 = 9;
                break;
            case "ㅏ":
                numTesx[0].setText("힘의 중심을 허리에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 10;
                break;
            case "ㅑ":
                numTesx[0].setText("힘의 중심을 등 가운데에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 13;
                e2 = 10;
                break;
            case "ㅓ":
                numTesx[0].setText("힘의 중심을 양쪽 엉치에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 13;
                e2 = 10;
                break;
            case "ㅕ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 9;
                break;
            case "ㅗ":
                numTesx[0].setText("힘의 중심을 골반에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 10;
                break;
            case "ㅛ":
                numTesx[0].setText("힘의 중심을 허리에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = e2 = 10;
                break;
            case "ㅜ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 9;
                break;
            case "ㅠ":
                numTesx[0].setText("힘의 중심을 아랫배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 양쪽 옆구리에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 11;
                e2 = 14;
                break;
            case "ㅡ":
                numTesx[0].setText("힘의 중심을 윗배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 등에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 10;
                e2 = 9;
                break;
            case "ㅣ":
                numTesx[0].setText("힘의 중심을 배에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                numTesx[1].setText("힘의 중심을 가슴에 두고 발성 후 한글호흡기로 숨을 들여 마신 뒤 숨을 참고 입을 다문 후 코로 숨을 내쉰다.");
                e1 = 9;
                e2 = 10;
                break;
        }
        SpannableStringBuilder t1 = new SpannableStringBuilder(numTesx[0].getText());
        SpannableStringBuilder t2 = new SpannableStringBuilder(numTesx[1].getText());
        SpannableStringBuilder t3 = new SpannableStringBuilder(numTesx[2].getText());
        SpannableStringBuilder t4 = new SpannableStringBuilder(numTesx[3].getText());
        SpannableStringBuilder t5 = new SpannableStringBuilder(numTesx[4].getText());
        SpannableStringBuilder t6 = new SpannableStringBuilder(numTesx[5].getText());
        t1.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")), 7, e1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t2.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")), 7, e2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t3.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t4.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")), 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t5.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")), 7, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        t6.setSpan(new ForegroundColorSpan(Color.parseColor("#5F00FF")), 7, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        numTesx[0].setText(t1);
        numTesx[1].setText(t2);
        numTesx[2].setText(t3);
        numTesx[3].setText(t4);
        numTesx[4].setText(t5);
        numTesx[5].setText(t6);
        for (int i = 0; i < numTextIds.length; i++) {
            final int index;
            index = i;
            numTesx[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (check == true) {
                        step = numTesx[index].getImeActionLabel().toString();
                        //Toast.makeText(getApplicationContext(), step, Toast.LENGTH_LONG).show();

                        if (numTesx[index].getImeActionLabel().toString().equals("STEP 1") || numTesx[index].getImeActionLabel().toString().equals("STEP 2")) {
                            contents = VIDEO_URL + letter + " " + numTesx[index].getImeActionLabel().toString() + ".mp3";
                        } else {
                            contents = VIDEO_URL + "공통 " + numTesx[index].getImeActionLabel().toString() + ".mp3";
                        }
                        videoView.setVideoURI(Uri.parse(contents));

                        check = false;
                        videoView.start();
                    } else {
                        check = true;
                        videoView.pause();
                    }
                }
            });
        }
    }

    public void play(View v) {
        videoView.setVideoURI(Uri.parse(VIDEO_URL + letter + "all" + ".mp3"));
        check = false;
        step = "ALL";
        videoView.start();
    }

    @Nullable
    public static String getHashKey(Context context) {
        final String TAG = "KeyHash";
        String keyHash = null;
        try {
            PackageInfo info =
                    context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = new String(Base64.encode(md.digest(), 0));
                Log.d(TAG, keyHash);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
        if (keyHash != null) {
            return keyHash;
        } else {
            return null;
        }
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    class InsertData_con extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String ids = (String) params[1];
            String contents = (String) params[2];
            String steps = (String) params[3];
            String chekceds = (String) params[4];
            String datez = (String) params[5];
            String time = (String) params[6];
            String serverURL = (String) params[0];
            String postParameters = "id=" + ids + "&content=" + contents + "&step=" + steps + "&checked=" + chekceds + "&date=" + datez + "&time=" + time;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                return new String("Error: " + e.getMessage());
            }
        }
    }
}