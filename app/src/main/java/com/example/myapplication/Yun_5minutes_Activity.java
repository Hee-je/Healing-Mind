package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Yun_5minutes_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    CountDownTimer countDownTimer;
    LinearLayout linear;
    int MILLISINFUTURE = 307 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;
    TextView show;
    VideoView videoView;
    Button play;
    DatePicker dday;
    boolean check = true;
    String[] txt = new String[48];
    String ID, IP, VIDEO_URL = "", TAG = "phpquerytest";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,    // 상단바
                WindowManager.LayoutParams.FLAG_FULLSCREEN);                        // 앲애기
        setContentView(R.layout.yun_5minutes_activity);
        videoView = (VideoView) findViewById(R.id.fullvideo);
        linear = (LinearLayout) findViewById(R.id.linear);
        show = findViewById(R.id.show);
        play = findViewById(R.id.play);
        videoView.setVideoURI(Uri.parse(VIDEO_URL));
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        dday = (DatePicker) findViewById(R.id.dpick);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        Integer y = dday.getYear();
        Integer mon = dday.getMonth() + 1;
        Integer mon2 = dday.getMonth();
        Integer day = dday.getDayOfMonth();
        final String calendar_date2 = "CalendarDay" + "{" + y.toString() + "-" + mon2.toString() + "-" + day.toString() + "}";
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
        String str = dayTime.format(new Date(time));
        Calendar c = Calendar.getInstance();
        String calendar_date = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        myHelper = new MyDBHelper(this);
        txt[0] = "잘 잤나요?";
        txt[1] = "자 먼저 편안한 자리에 앉습니다";
        txt[2] = "눈을 감습니다";
        txt[3] = "허리를 바르게 폅니다";
        txt[4] = "지금부터 30초 동안 손바닥을 가볍게 비비겠습니다";
        txt[5] = "자 그리고 손바닥이 위로 향하도록 무릎에 편하게 내려놓습니다";
        txt[6] = "편안하게 숨을 고르세요";
        txt[7] = "여전히 손바닥은 위로 향하도록 편하게 내려놓습니다";
        txt[8] = "당신의 손에 묵직한 느낌이 느껴지실 겁니다";
        txt[9] = "그것이 바로 기의 느낌입니다";
        txt[10] = "그리고 이 느낌은 당신의 온 몸으로 퍼져나갑니다";
        txt[11] = "은은하게 당신의 온 몸에 퍼집니다";
        txt[12] = "당신의 온 몸은 따뜻하게 느껴집니다";
        txt[13] = "다시 한 번 심호흡을 두 번 반복하겠습니다";
        txt[14] = "내 몸은 오늘 아침 다시 태어났습니다";
        txt[15] = "오늘 또 나에게 하루가 주어졌음에 감사합니다";
        txt[16] = "지금부터 당신은 오늘 해야할 일을 떠올려 봅니다";
        txt[17] = "그리고 오늘 당신이 만나야 할 사람들을 떠올려 봅니다";
        txt[18] = "편안하게 당신이 오늘 만나야 할 사람들 오늘 해야 할 일들";
        txt[19] = "오늘 당신의 과제를 떠올려 봅니다";
        txt[20] = "당신이 만나야 될 사람들";
        txt[21] = "그 사람들을 만나고 웃는 모습을 떠올려 봅니다";
        txt[22] = "그 사람들을 만나고 당신은 행복해 합니다";
        txt[23] = "당신은 그 사람들을 만나고 즐거워 합니다";
        txt[24] = "그런 모습이 참 보기 좋습니다";
        txt[25] = "당신이 오늘 헤쳐나가야 될 과제를 떠올려 봅니다";
        txt[26] = "당신은 그걸 잘했습니다";
        txt[27] = "그 일을 잘 마무리 짓고 웃는 모습을 떠올려 봅니다";
        txt[28] = "성공적으로 그 일을 헤쳐나갔고";
        txt[29] = "행복해하는 당신의 모습을 떠올려 봅니다";
        txt[30] = "당신은 참 그 과제를 잘했습니다";
        txt[31] = "당신이 해야 할 일들을 떠올려 봅니다";
        txt[32] = "그 일을 하면서 즐거워 하는 당신을 떠올려 봅니다";
        txt[33] = "당신은 그 일에 능숙합니다";
        txt[34] = "그 일에 자신이 있습니다";
        txt[35] = "조금은 힘들지만 그 일은 나에게 행복을 주는 일입니다";
        txt[36] = "그 일로 당신은 인정받습니다";
        txt[37] = "많은 사람들이 당신을 칭찬합니다";
        txt[38] = "그 일로 행복해하는 나의 모습이 떠오릅니다";
        txt[39] = "그 모습이 당신의 모습입니다";
        txt[40] = "당신은 오늘 참 잘할겁니다";
        txt[41] = "당신이 오늘 하루의 주인공입니다";
        txt[42] = "당신의 생각은 오늘 하루를 만듭니다";
        txt[43] = "당신이 지금 하는 상상과 이 생각을 잊지 마세요";
        txt[44] = "당신은 오늘 성공된 하루, 행복한 하루, 그 중심의 주인공입니다";
        txt[45] = "당신은 오늘 참 잘할 겁니다";
        txt[46] = "당신은 참 행복할 겁니다";
        txt[47] = "오늘도 당신 곁에 있을게요";
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check) {
                    NetworkInfo mNetworkState = getNetworkInfo();
                    if (mNetworkState != null && mNetworkState.isConnected()) {
                        if (mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Yun_5minutes_Activity.this);
                            builder.setCancelable(false);
                            builder.setTitle("Wi-fi가 아닌 이동통신망에 접속된 상태에서는 별도의 데이터 통화료가 부과될 수 있습니다");
                            builder.setMessage("재생하시겠습니까?");
                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            check = false;
                                            videoView.start();
                                            countDownTimer();
                                            countDownTimer.start();
                                            play.setVisibility(View.INVISIBLE);
                                            GetData setCon2 = new GetData();
                                            setCon2.execute(ID, calendar_date, str);
                                            Cursor recently_cursor;
                                            sqlDB = myHelper.getReadableDatabase();
                                            recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + "" + "' AND title ='" + "5분명상" + "';", null);
                                            if (!recently_cursor.moveToNext()) {
                                                sqlDB = myHelper.getWritableDatabase();
                                                sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/one" + "','" + "5분명상" + "','" + "" + "');");
                                            }
                                            recently_cursor.close();
                                            sqlDB.close();

                                            Cursor cursor2;
                                            sqlDB = myHelper.getReadableDatabase();
                                            cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date2 + "' AND schedule ='" + "5분명상" + "';", null);
                                            if (!cursor2.moveToNext()) {
                                                sqlDB = myHelper.getWritableDatabase();
                                                sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date2 + "','" + "5분명상" + "');");
                                            }
                                            cursor2.close();
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
                                        }
                                    });
                            builder.setNegativeButton("취소", null);
                            builder.create().show();
                        } else {
                            check = false;
                            videoView.start();
                            countDownTimer();
                            countDownTimer.start();
                            play.setVisibility(View.INVISIBLE);
                            GetData setCon2 = new GetData();
                            setCon2.execute(ID, calendar_date, str);
                            Cursor recently_cursor;
                            sqlDB = myHelper.getReadableDatabase();
                            recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + "" + "' AND title ='" + "5분명상" + "';", null);
                            if (!recently_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/one" + "','" + "5분명상" + "','" + "" + "');");
                            }
                            recently_cursor.close();
                            sqlDB.close();

                            Cursor cursor2;
                            sqlDB = myHelper.getReadableDatabase();
                            cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date2 + "' AND schedule ='" + "5분명상" + "';", null);
                            if (!cursor2.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date2 + "','" + "5분명상" + "');");
                            }
                            cursor2.close();
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
                        }
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Yun_5minutes_Activity.this);
                    builder.setCancelable(false);
                    builder.setTitle("인터넷 연결을 확인해 주세요");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }
            }
        });
        MediaController controller = new MediaController(Yun_5minutes_Activity.this);
        videoView.setMediaController(controller);
        videoView.requestFocus();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                check = true;
            }
        });
    }

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                long emailAuthCount = (MILLISINFUTURE / 1000) - (millisUntilFinished / 1000);
                switch (String.valueOf(emailAuthCount)) {
                    case "3":
                        Animation anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);

                        show.setText(txt[0]);
                        break;
                    case "6":
                        Animation anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[1]);
                        break;
                    case "10":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "14":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[2]);
                        break;
                    case "17":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "20":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[3]);
                        break;
                    case "23":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[4]);
                        break;
                    case "31":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "58":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[5]);
                        break;
                    case "68":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[6]);
                        break;
                    case "72":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[7]);
                        break;
                    case "81":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[8]);
                        break;
                    case "86":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[9]);
                        break;
                    case "89":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[10]);
                        break;
                    case "93":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[11]);
                        break;
                    case "98":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "100":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[12]);
                        break;
                    case "105":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "107":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[13]);
                        break;
                    case "110":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "126":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[14]);
                        break;
                    case "131":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[15]);
                        break;
                    case "136":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[16]);
                        break;
                    case "143":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[17]);
                        break;
                    case "148":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[18]);
                        break;
                    case "155":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[19]);
                        break;
                    case "160":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "162":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[20]);
                        break;
                    case "164":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[21]);
                        break;
                    case "169":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "172":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[22]);
                        break;
                    case "176":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[23]);
                        break;
                    case "181":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[24]);
                        break;
                    case "184":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "187":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[25]);
                        break;
                    case "192":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "195":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[26]);
                        break;
                    case "197":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "199":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[27]);
                        break;
                    case "205":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[28]);
                        break;
                    case "208":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[29]);
                        break;
                    case "213":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[30]);
                        break;
                    case "216":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "218":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[31]);
                        break;
                    case "222":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "223":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[32]);
                        break;
                    case "228":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[33]);
                        break;
                    case "232":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[34]);
                        break;
                    case "235":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[35]);
                        break;
                    case "241":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[36]);
                        break;
                    case "245":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "247":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[37]);
                        break;
                    case "251":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "253":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[38]);
                        break;
                    case "257":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[39]);
                        break;
                    case "260":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                    case "261":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[40]);
                        break;
                    case "265":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[41]);
                        break;
                    case "269":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[42]);
                        break;
                    case "273":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[43]);
                        break;
                    case "278":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[44]);
                        break;
                    case "286":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[45]);
                        break;
                    case "291":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[46]);
                        break;
                    case "295":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText(txt[47]);
                        break;
                    case "300":
                        anim2 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha2_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim2);
                        anim1 = AnimationUtils.loadAnimation
                                (getApplicationContext(), // 현재화면 제어권자
                                        R.anim.alpha_anim);      // 에니메이션 설정한 파일
                        show.startAnimation(anim1);
                        show.setText("");
                        break;
                }
            }

            public void onFinish() {
                countDownTimer.cancel();
                check = true;
                play.setVisibility(View.VISIBLE);
            }
        };
    }

    class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String date = params[1];
            String time = params[2];
            String serverURL = "http://" + IP + "/contents.php";
            String postParameters = "id=" + id + "&content=" + "5분" + "&step=" + "" + "&checked=" + "" + "&date=" + date + "&time=" + time;

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
                Log.d(TAG, "UpdateData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}