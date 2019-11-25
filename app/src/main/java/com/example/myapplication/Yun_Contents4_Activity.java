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
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class Yun_Contents4_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    ImageView[] numTesx = new ImageView[5];
    ImageView img;
    LinearLayout linear;
    Integer[] imgs = new Integer[5];
    Integer[] numTextIds = {R.id.img1, R.id.img2, R.id.img3, R.id.img4, R.id.img5};
    Integer img_count = 1, end = 5, choiced;
    String ID, IP, category, VIDEO_URL, category_k, TAG = "phpquerytest", IP_Contents = "";
    Boolean select = false, change = false;
    DatePicker dday;
    VideoView videoView;
    TextView tmi;
    Button video_play;
    CheckBox re_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_contents4_activity);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        VIDEO_URL = "http://" + IP_Contents;
        category = intent.getStringExtra("category");
        category_k = intent.getStringExtra("category_k");
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("소리 명상 (" + category_k + ")");
        dday = (DatePicker) findViewById(R.id.dpick);
        videoView = (VideoView) findViewById(R.id.fullvideo);
        tmi = (TextView) findViewById(R.id.tmi);
        img = (ImageView) findViewById(R.id.img);
        video_play = (Button) findViewById(R.id.video_play);
        linear = (LinearLayout) findViewById(R.id.linear);
        re_check = (CheckBox) findViewById(R.id.re_check);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        myHelper = new MyDBHelper(this);
        Cursor cursor;
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + category_k + "' AND title ='" + "소리" + "';", null);
        if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
            img.setImageResource(R.mipmap.heart_1);
        } else { // 즐겨찾기를 했을때
            img.setImageResource(R.mipmap.heart_2);
        }
        cursor.close();
        sqlDB.close();
        Cursor recently_cursor;
        sqlDB = myHelper.getReadableDatabase();
        recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + category_k + "' AND title ='" + "소리" + "';", null);
        if (!recently_cursor.moveToNext()) {
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/four" + "','" + "소리" + "','" + category_k + "');");
        }
        recently_cursor.close();
        sqlDB.close();
        Integer y = dday.getYear();
        Integer mon = dday.getMonth() + 1;
        Integer mon2 = dday.getMonth();
        Integer day = dday.getDayOfMonth();
        String calendar_date = "CalendarDay" + "{" + y.toString() + "-" + mon2.toString() + "-" + day.toString() + "}";
        String schedule = "소리 - " + category_k;
        Cursor cursor2;
        sqlDB = myHelper.getReadableDatabase();
        cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "'AND schedule = '" + schedule + "';", null);
        if (!cursor2.moveToNext()) {
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + schedule + "');");
        }
        Cursor attendance_cursor;
        sqlDB = myHelper.getReadableDatabase();
        attendance_cursor = sqlDB.rawQuery("SELECT *FROM attendance WHERE _id = '" + ID + "' AND year = '" + y.toString() + "'AND month ='" + mon.toString() + "'AND day ='" + day.toString() + "';", null);
        if (!attendance_cursor.moveToNext()) {
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO attendance VALUES (null,'" + ID + "','" + y.toString() + "','" + mon.toString() + "','" + day.toString() + "');");
        }
        attendance_cursor.close();
        sqlDB.close();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor;
                sqlDB = myHelper.getReadableDatabase();
                cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + category_k + "' AND title ='" + "소리" + "';", null);
                if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO bookmark VALUES (NULL, '" + ID + "','" + "mipmap/four" + "','" + "소리" + "','" + category_k + "');");
                    img.setImageResource(R.mipmap.heart_2);
                } else { // 즐겨찾기를 했을때
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("DELETE FROM bookmark WHERE _id =  '" + ID + "' AND title = '" + "소리" + "'AND letter = '" + category_k + "'");
                    img.setImageResource(R.mipmap.heart_1);
                }
                cursor.close();
                sqlDB.close();
            }
        });
        if (category_k.equals("비")) {
            tmi.setText("\"빗속에서 안정을 찾는다\"");
            imgs[0] = R.mipmap.rain_1;
            imgs[1] = R.mipmap.rain_2;
            imgs[2] = R.mipmap.rain_3;
            imgs[3] = R.mipmap.rain_4;
            imgs[4] = R.mipmap.rain_5;
            choiced = 3;
        } else if (category_k.equals("바다")) {
            tmi.setText("\"파도가 부딪히는 소리\"");
            imgs[0] = R.mipmap.sea_1;
            imgs[1] = R.mipmap.sea_2;
            imgs[2] = R.mipmap.sea_3;
            imgs[3] = R.mipmap.sea_4;
            imgs[4] = R.mipmap.sea_5;
            choiced = 4;
        } else if (category_k.equals("눈")) {
            tmi.setText("\"새벽녘의 눈 오는 어느 날\"");
            imgs[0] = R.mipmap.snow_1;
            imgs[1] = R.mipmap.snow_2;
            choiced = 8;
        } else if (category_k.equals("장작")) {
            tmi.setText("\"따스한 온기가 가득한 어느 밤\"");
            imgs[0] = R.mipmap.wood_1;
            imgs[1] = R.mipmap.wood_2;
            imgs[2] = R.mipmap.wood_3;
            imgs[3] = R.mipmap.wood_4;
            imgs[4] = R.mipmap.wood_5;
            choiced = 6;
        } else if (category_k.equals("계곡")) {
            tmi.setText("\"산오름 끝에 다다른 계곡\"");
            imgs[0] = R.mipmap.valley_1;
            imgs[1] = R.mipmap.valley_2;
            imgs[2] = R.mipmap.valley_3;
            imgs[3] = R.mipmap.valley_4;
            imgs[4] = R.mipmap.valley_5;
            choiced = 1;
        } else if (category_k.equals("산")) {
            tmi.setText("\"산에서 들려오는 자연의 소리\"");
            imgs[0] = R.mipmap.mountain_1;
            imgs[1] = R.mipmap.mountain_2;
            imgs[2] = R.mipmap.mountain_3;
            imgs[3] = R.mipmap.mountain_4;
            imgs[4] = R.mipmap.mountain_5;
            choiced = 2;
        } else if (category_k.equals("종")) {
            tmi.setText("\"마음 속 깊은 곳에서 울려퍼지다\"");
            imgs[0] = R.mipmap.bell_1;
            imgs[1] = R.mipmap.bell_2;
            imgs[2] = R.mipmap.bell_3;
            imgs[3] = R.mipmap.bell_4;
            imgs[4] = R.mipmap.bell_5;
            choiced = 7;
        } else if (category_k.equals("심장")) {
            tmi.setText("\"가장 가까운 소리\"");
            imgs[0] = R.mipmap.heart_sound_1;
            imgs[1] = R.mipmap.heart_sound_2;
            choiced = 5;
        }
        if (category.equals("snow") || category.equals("heart_sound")) {
            end = 2;
        }
        for (int i = 0; i < numTextIds.length; i++) {
            numTesx[i] = (ImageView) findViewById(numTextIds[i]);
        }
        for (int j = 0; j < numTextIds.length; j++) {

            numTesx[j].getLayoutParams().height = 1000;
            numTesx[j].getLayoutParams().width = 800;
            numTesx[j].requestLayout();
        }
        for (int i = 0; i < end; i++) {
            final int index;
            index = i;
            numTesx[index].setVisibility(View.VISIBLE);
            numTesx[index].setImageResource(imgs[i]);
        }
        img_count++;
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // 재생완료
                videoView.seekTo(0);
                if (re_check.isChecked()) {
                    video_play.setText("정지");
                    videoView.start();
                } else {
                    video_play.setText("플레이");
                }
            }
        });
        for (int i = 0; i < numTextIds.length; i++) {
            final Integer index, number;
            index = i;
            number = i + 1;
            numTesx[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j < numTextIds.length; j++) {
                        if (index != j) {
                            numTesx[j].getLayoutParams().height = 1000;
                            numTesx[j].getLayoutParams().width = 800;
                        } else {
                            numTesx[j].getLayoutParams().height = 1100;
                            numTesx[j].getLayoutParams().width = 850;
                        }
                        numTesx[j].requestLayout();
                    }
                    try {
                        select = change = true;
                        video_play.setText("플레이");
                        videoView.setVideoURI(Uri.parse(VIDEO_URL + "/contents/" + category + "/" + category + "_" + number + ".mp3"));
                        videoView.pause();
                        videoView.seekTo(0);
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    public void play(View v) {
        if (videoView.isPlaying()) {
            video_play.setText("플레이");
            videoView.pause();
        } else if (select) {
            NetworkInfo mNetworkState = getNetworkInfo();
            if (mNetworkState != null && mNetworkState.isConnected()) {
                if (change) {
                    if (mNetworkState.getType() == ConnectivityManager.TYPE_MOBILE) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Contents4_Activity.this);
                        builder.setCancelable(false);
                        builder.setTitle("Wi-fi가 아닌 이동통신망에 접속된 상태에서는 별도의 데이터 통화료가 부과될 수 있습니다");
                        builder.setMessage("재생하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                change = false;
                                video_play.setText("정지");
                                videoView.start();
                                Calendar c = Calendar.getInstance();
                                String dates = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                                long time = System.currentTimeMillis();
                                SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                                String str = dayTime.format(new Date(time));
                                InsertData_con task_con = new InsertData_con();
                                task_con.execute("http://" + IP + "/contents.php", ID, "소리", choiced.toString(), category_k, dates, str);
                            }
                        });
                        builder.setNegativeButton("취소", null);
                        builder.create().show();
                    } else {
                        change = false;
                        video_play.setText("정지");
                        videoView.start();
                        Calendar c = Calendar.getInstance();
                        String dates = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                        String str = dayTime.format(new Date(time));
                        InsertData_con task_con = new InsertData_con();
                        task_con.execute("http://" + IP + "/contents.php", ID, "소리", choiced.toString(), category_k, dates, str);
                    }
                } else {
                    video_play.setText("정지");
                    videoView.start();
                    Calendar c = Calendar.getInstance();
                    String dates = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                    long time = System.currentTimeMillis();
                    SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                    String str = dayTime.format(new Date(time));
                    InsertData_con task_con = new InsertData_con();
                    task_con.execute("http://" + IP + "/contents.php", ID, "소리", choiced.toString(), category_k, dates, str);
                }
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Contents4_Activity.this);
                builder.setCancelable(false);
                builder.setTitle("인터넷 연결을 확인해 주세요");
                builder.setPositiveButton("확인", null);
                builder.create().show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Contents4_Activity.this);
            builder.setTitle("원하시는 명상 소리를 선택해 주세요");
            builder.setPositiveButton("확인", null);
            builder.create().show();
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
            String timez = (String) params[6];
            String serverURL = (String) params[0];
            String postParameters = "id=" + ids + "&content=" + contents + "&step=" + steps + "&checked=" + chekceds + "&date=" + datez + "&time=" + timez;
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