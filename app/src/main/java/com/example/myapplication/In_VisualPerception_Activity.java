package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
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
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.content.Context;

import com.example.myapplication.devadvance.circularseekbar.CircularSeekBar;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class In_VisualPerception_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    private static final String TAG = "phpquerytest";
    String ID, IP, mJsonString, letter, VIDEO_URL, TimetoString, IP_Contents = "";
    boolean pass = true;
    VideoView videoView;
    CircularSeekBar seekbar, seekbar2, seekbar3;
    boolean check = true;
    DatePicker dday;
    Integer num;
    ProgressBar progress;
    ImageView img, ball;
    LinearLayout linear;
    Animation anima1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_visualperception_tool);
        FloatingActionButton start = (FloatingActionButton) findViewById(R.id.start);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        letter = intent.getStringExtra("letter");
        VIDEO_URL = "http://" + IP_Contents + "/mp3/" + letter + ".mp3";
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("한글 시지각 명상 (" + letter + ")");
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        NetworkInfo mNetworkState = getNetworkInfo();
        if (mNetworkState != null && mNetworkState.isConnected()) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(In_VisualPerception_Activity.this);
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
        img = (ImageView) findViewById(R.id.img);
        ball = (ImageView) findViewById(R.id.ball);

        anima1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cycle1);
        anima1.setInterpolator(new LinearInterpolator());
        ball.startAnimation(anima1);

        dday = (DatePicker) findViewById(R.id.dpick);
        seekbar = (CircularSeekBar) findViewById(R.id.circularSeekBar1);
        seekbar2 = (CircularSeekBar) findViewById(R.id.circularSeekBar2);
        seekbar3 = (CircularSeekBar) findViewById(R.id.circularSeekBar3);
        TextView lettera = (TextView) findViewById(R.id.letter);
        TextView caution = (TextView) findViewById(R.id.caution);
        lettera.setText(letter);
        videoView = (VideoView) findViewById(R.id.fullvideo);
        progress = (ProgressBar) findViewById(R.id.progress);
        MediaController controller = new MediaController(In_VisualPerception_Activity.this);
        videoView.setMediaController(controller);
        videoView.requestFocus();
        myHelper = new MyDBHelper(this);
        Integer y = dday.getYear();
        Integer mon = dday.getMonth() + 1;
        Integer mon2 = dday.getMonth();
        Integer day = dday.getDayOfMonth();
        final String calendar_date = "CalendarDay" + "{" + y.toString() + "-" + mon2.toString() + "-" + day.toString() + "}";
        caution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(In_VisualPerception_Activity.this);
                builder.setTitle("주의사항");
                builder.setMessage("※ 등과 허리를 바르게 펴고 앉는다.\n※ 왼손으로 스마트폰을 잡고 오른손으로 바깥 방향에서 안쪽 방향으로 중심을 향해 원을 그리며 3초간 발성을 한 후 2초간 공기를 들이마신 뒤 코로 숨을 내쉰다.");
                builder.show();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                check = true;
            }
        });
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
        myHelper = new MyDBHelper(this);
        Cursor cursor;
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM visualperception WHERE _id = '" + ID + "' AND letter ='" + letter + "';", null);
        if (!cursor.moveToNext()) {
            sqlDB = myHelper.getWritableDatabase();
            sqlDB.execSQL("INSERT INTO visualperception VALUES (null,'" + ID + "','" + letter + "','" + "0" + "');");
        }
        cursor.close();
        sqlDB.close();
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM visualperception WHERE _id = '" + ID + "' AND letter ='" + letter + "';", null);
        cursor.moveToNext();
        num = Integer.parseInt(cursor.getString(3));
        progress.setProgress(100 * num);
        cursor.close();
        sqlDB.close();
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "시지각" + "';", null);
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
                cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "시지각" + "';", null);
                if (!cursor.moveToNext()) { // 즐겨찾기를 안했을때
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO bookmark VALUES (NULL, '" + ID + "','" + "mipmap/three" + "','" + "시지각" + "','" + letter + "');");
                    img.setImageResource(R.mipmap.heart_2);
                } else { // 즐겨찾기를 했을때
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("DELETE FROM bookmark WHERE _id =  '" + ID + "' AND title = '" + "시지각" + "'AND letter = '" + letter + "'");
                    img.setImageResource(R.mipmap.heart_1);
                }
                cursor.close();
                sqlDB.close();
            }
        });
        seekbar3.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                try {
                    if (seekbar3.getProgress() == 100)
                        seekbar3.setIsTouchEnabled(false);
                    if (seekbar2.getProgress() == 100 && seekbar.getProgress() == 100 && seekbar3.getProgress() == 100 && pass) {
                        Cursor cursor;
                        sqlDB = myHelper.getReadableDatabase();
                        cursor = sqlDB.rawQuery("SELECT *FROM visualperception WHERE _id = '" + ID + "' AND letter = '" + letter + "';", null);
                        cursor.moveToNext();
                        if (cursor.getString(3).equals("0")) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("UPDATE visualperception SET seekbar = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                        }
                        cursor.close();
                        sqlDB.close();
                        pass = false;
                        Calendar c = Calendar.getInstance();
                        TimetoString = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                        String str = dayTime.format(new Date(time));
                        GetData setCon2 = new GetData();
                        setCon2.execute(ID, letter, TimetoString, str);
                        String visual = letter + " - 한글 시지각 영상";
                        Cursor cursor2;
                        sqlDB = myHelper.getReadableDatabase();
                        cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "' AND schedule ='" + visual + "';", null);
                        if (!cursor2.moveToNext()) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + visual + "');");
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
                        progressed pgr = new progressed();
                        pgr.setProgressed(100);
                        Cursor recently_cursor;
                        sqlDB = myHelper.getReadableDatabase();
                        recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "시지각" + "';", null);
                        if (!recently_cursor.moveToNext()) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/three" + "','" + "시지각" + "','" + letter + "');");
                        }
                        recently_cursor.close();
                        sqlDB.close();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        });
        seekbar2.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                try {
                    if (seekbar2.getProgress() == 100)
                        seekbar2.setIsTouchEnabled(false);
                    if (seekbar2.getProgress() == 100 && seekbar.getProgress() == 100 && seekbar3.getProgress() == 100 && pass) {
                        Cursor cursor;
                        sqlDB = myHelper.getReadableDatabase();
                        cursor = sqlDB.rawQuery("SELECT *FROM visualperception WHERE _id = '" + ID + "' AND letter = '" + letter + "';", null);
                        cursor.moveToNext();
                        if (cursor.getString(3).equals("0")) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("UPDATE visualperception SET seekbar = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                        }
                        cursor.close();
                        sqlDB.close();
                        pass = false;
                        Calendar c = Calendar.getInstance();
                        TimetoString = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                        String str = dayTime.format(new Date(time));
                        GetData setCon2 = new In_VisualPerception_Activity.GetData();
                        setCon2.execute(ID, letter, TimetoString, str);
                        String visual = letter + " - 한글 시지각 영상";
                        Cursor cursor2;
                        sqlDB = myHelper.getReadableDatabase();
                        cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "' AND schedule ='" + visual + "';", null);
                        if (!cursor2.moveToNext()) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + visual + "');");
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
                        progressed pgr = new progressed();
                        pgr.setProgressed(100);
                        Cursor recently_cursor;
                        sqlDB = myHelper.getReadableDatabase();
                        recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "시지각" + "';", null);
                        if (!recently_cursor.moveToNext()) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/three" + "','" + "시지각" + "','" + letter + "');");
                        }
                        recently_cursor.close();
                        sqlDB.close();
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
            }
        });
        seekbar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, int progress, boolean fromUser) {
                try {
                    if (seekbar.getProgress() == 100)
                        seekbar.setIsTouchEnabled(false);
                    if (seekbar2.getProgress() == 100 && seekbar.getProgress() == 100 && seekbar3.getProgress() == 100 && pass) {
                        Cursor cursor;
                        sqlDB = myHelper.getReadableDatabase();
                        cursor = sqlDB.rawQuery("SELECT *FROM visualperception WHERE _id = '" + ID + "' AND letter = '" + letter + "';", null);
                        cursor.moveToNext();
                        if (cursor.getString(3).equals("0")) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("UPDATE visualperception SET seekbar = '" + "1" + "'WHERE _id = '" + ID + "'AND letter = '" + letter + "'");
                        }
                        cursor.close();
                        sqlDB.close();
                        pass = false;
                        Calendar c = Calendar.getInstance();
                        TimetoString = String.format("%04d-%02d-%02d", c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH));
                        long time = System.currentTimeMillis();
                        SimpleDateFormat dayTime = new SimpleDateFormat("kk:mm");
                        String str = dayTime.format(new Date(time));
                        GetData setCon2 = new In_VisualPerception_Activity.GetData();
                        setCon2.execute(ID, letter, TimetoString, str);
                        String visual = letter + " - 한글 시지각 영상";
                        Cursor cursor2;
                        sqlDB = myHelper.getReadableDatabase();
                        cursor2 = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day ='" + calendar_date + "' AND schedule ='" + visual + "';", null);
                        if (!cursor2.moveToNext()) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO calendar VALUES (null,'" + ID + "','" + calendar_date + "','" + visual + "');");
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
                        progressed pgr = new progressed();
                        pgr.setProgressed(100);
                        Cursor recently_cursor;
                        sqlDB = myHelper.getReadableDatabase();
                        recently_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "' AND letter ='" + letter + "' AND title ='" + "시지각" + "';", null);
                        if (!recently_cursor.moveToNext()) {
                            sqlDB = myHelper.getWritableDatabase();
                            sqlDB.execSQL("INSERT INTO recently VALUES (null,'" + ID + "','" + "mipmap/three" + "','" + "시지각" + "','" + letter + "');");
                        }
                        recently_cursor.close();
                        sqlDB.close();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {
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
            String time = (String) params[3];
            String serverURL = "http://" + IP + "/contents.php";
            String postParameters = "id=" + id + "&content=" + "시지각" + "&step=" + "0" + "&checked=" + letter + "&date=" + date + "&time=" + time;
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

    class progressed {
        void setProgressed(int num) {
            progress.setProgress(num);
        }
    }
}