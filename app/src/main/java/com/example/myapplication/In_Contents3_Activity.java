package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class In_Contents3_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    LinearLayout linear;
    String ID, IP, mJsonString, letter, VIDEO_URL, TimetoString, IP_contents = "";
    private static final String TAG = "phpquerytest";
    VideoView videoView;
    ImageView ball, start, img;
    ProgressBar progress;
    DatePicker dday;
    TextView caution;
    String line = "아직안받음";
    Animation anima1;
    LinearLayout pic;
    boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_contents3_tool);
        FloatingActionButton start = (FloatingActionButton) findViewById(R.id.start);
        ball = (ImageView) findViewById(R.id.ball);
        linear = (LinearLayout) findViewById(R.id.linear);
        pic = (LinearLayout) findViewById(R.id.pic);
        img = (ImageView) findViewById(R.id.img);
        videoView = findViewById(R.id.fullvideo);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        letter = intent.getStringExtra("letter");
        NetworkInfo mNetworkState = getNetworkInfo();
        if (mNetworkState != null && mNetworkState.isConnected()) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(In_Contents3_Activity.this);
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
        VIDEO_URL = "http://" + IP_contents + "/mp3/" + letter + ".mp3";
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("한글 그리기 명상 (" + letter + ")");
        caution = findViewById(R.id.caution);
        line = intent.getStringExtra("line");
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setProgress(100);
        String a = "";
        if (line.equals("straight")) {
            a = "그리기 - 직선";
        } else if (line.equals("zigzag")) {
            a = "그리기 - 지그재그";
        } else if (line.equals("anfruf")) {
            a = "그리기 - 물결";
        }
        myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + a + "';", null);
        if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
            img.setImageResource(R.mipmap.heart_1);
        } else { // 즐겨찾기를 했을때
            img.setImageResource(R.mipmap.heart_2);
        }
        cursor.close();
        sqlDB.close();
        caution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(In_Contents3_Activity.this);
                builder.setTitle("주의사항");
                builder.setMessage("※ 등과 허리를 바르게 펴고 앉는다.\n※ 왼손으로 스마트폰을 잡고 오른손으로 원을 따라 선을 그리며 3초간 발성을 한 후 2초간 공기를 들이마신 뒤 코로 숨을 내쉰다.");
                builder.show();
            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (line.equals("straight")) {
                    Cursor cursor;
                    sqlDB = myHelper.getReadableDatabase();
                    cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "그리기 - 직선" + "';", null);
                    if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO bookmark VALUES (NULL, '" + ID + "','" + "mipmap/five" + "','" + "그리기 - 직선" + "','" + letter + "');");
                        img.setImageResource(R.mipmap.heart_2);
                    } else { // 즐겨찾기를 했을때
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("DELETE FROM bookmark WHERE _id =  '" + ID + "' AND title = '" + "그리기 - 직선" + "'AND letter = '" + letter + "'");
                        img.setImageResource(R.mipmap.heart_1);
                    }
                    cursor.close();
                    sqlDB.close();
                } else if (line.equals("zigzag")) {
                    Cursor cursor;
                    sqlDB = myHelper.getReadableDatabase();
                    cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "그리기 - 지그재그" + "';", null);
                    if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO bookmark VALUES (NULL, '" + ID + "','" + "mipmap/five" + "','" + "그리기 - 지그재그" + "','" + letter + "');");
                        img.setImageResource(R.mipmap.heart_2);
                    } else { // 즐겨찾기를 했을때
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("DELETE FROM bookmark WHERE _id =  '" + ID + "' AND title = '" + "그리기 - 지그재그" + "'AND letter = '" + letter + "'");
                        img.setImageResource(R.mipmap.heart_1);
                    }
                    cursor.close();
                    sqlDB.close();
                } else if (line.equals("anfruf")) {
                    Cursor cursor;
                    sqlDB = myHelper.getReadableDatabase();
                    cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "그리기 - 물결" + "';", null);
                    if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("INSERT INTO bookmark VALUES (NULL, '" + ID + "','" + "mipmap/five" + "','" + "그리기 - 물결" + "','" + letter + "');");
                        img.setImageResource(R.mipmap.heart_2);
                    } else { // 즐겨찾기를 했을때
                        sqlDB = myHelper.getWritableDatabase();
                        sqlDB.execSQL("DELETE FROM bookmark WHERE _id =  '" + ID + "' AND title = '" + "그리기 - 물결" + "'AND letter = '" + letter + "'");
                        img.setImageResource(R.mipmap.heart_1);
                    }
                    cursor.close();
                    sqlDB.close();
                }
            }
        });
        Cursor recently_cursor;
        sqlDB = myHelper.getReadableDatabase();
        if (line.equals("straight")) {
            recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "그리기 - 직선" + "';", null);
            if (!recently_cursor.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/five" + "','" + "그리기 - 직선" + "','" + letter + "');");
            }
            recently_cursor.close();
            sqlDB.close();
        } else if (line.equals("zigzag")) {
            recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "그리기 - 지그재그" + "';", null);
            if (!recently_cursor.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/five" + "','" + "그리기 - 지그재그" + "','" + letter + "');");
            }
            recently_cursor.close();
            sqlDB.close();
        } else if (line.equals("anfruf")) {
            recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "그리기 - 물결" + "';", null);
            if (!recently_cursor.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/five" + "','" + "그리기 - 물결" + "','" + letter + "');");
            }
            recently_cursor.close();
            sqlDB.close();
        }
        dday = (DatePicker) findViewById(R.id.dpick);
        Integer y = dday.getYear();
        Integer mon = dday.getMonth() + 1;
        Integer mon2 = dday.getMonth();
        Integer day = dday.getDayOfMonth();
        String calendar_date = "CalendarDay" + "{" + y.toString() + "-" + mon2.toString() + "-" + day.toString() + "}";
        if (line.equals("straight")) {
            String draw = letter + " - 그리기(직선)";
            Cursor cursor2;
            sqlDB = myHelper.getReadableDatabase();
            cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "' AND schedule ='" + draw + "';", null);
            if (!cursor2.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + draw + "');");
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
        } else if (line.equals("zigzag")) {
            String draw = letter + " - 그리기(지그재그)";
            Cursor cursor2;
            sqlDB = myHelper.getReadableDatabase();
            cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "' AND schedule ='" + draw + "';", null);
            if (!cursor2.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + draw + "');");
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
        } else if (line.equals("anfruf")) {
            String draw = letter + " - 그리기(물결)";
            Cursor cursor2;
            sqlDB = myHelper.getReadableDatabase();
            cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "' AND schedule ='" + draw + "';", null);
            if (!cursor2.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + draw + "');");
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
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check == true) {
                    videoView.setVideoURI(Uri.parse(VIDEO_URL));
                    check = false;
                    videoView.start();
                } else if (check == false) {
                    videoView.pause();
                }
            }
        });
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
        String str = dayTime.format(new Date(time));
        Calendar c = Calendar.getInstance();
        TimetoString = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
        GetData setCon2 = new GetData();
        if (line.equals("zigzag")) {
            anima1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zigzag);
            anima1.setInterpolator(new LinearInterpolator());
            ball.startAnimation(anima1);
            pic.setBackgroundResource(R.drawable.zigzag);
            setCon2.execute(ID, letter, TimetoString, "지그재그", str);
        } else if (line.equals("straight")) {
            anima1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.straight);
            anima1.setInterpolator(new LinearInterpolator());
            ball.startAnimation(anima1);
            pic.setBackgroundResource(R.drawable.straight);
            setCon2.execute(ID, letter, TimetoString, "직선", str);
        } else if (line.equals("anfruf")) {
            anima1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anfruf);
            ball.startAnimation(anima1);
            pic.setBackgroundResource(R.drawable.anfruf);
            setCon2.execute(ID, letter, TimetoString, "물결", str);
        }
        anima1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ball.startAnimation(anima1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String letter = params[1];
            String date = params[2];
            String con = params[3];
            String time = params[4];
            String serverURL = "http://" + IP + "/contents.php";
            String postParameters = "id=" + id + "&content=" + "그리기" + "&step=" + con + "&checked=" + letter + "&date=" + date + "&time=" + time;
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
}