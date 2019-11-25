package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Yun_BoardUpload_Activity extends AppCompatActivity {
    EditText titles, memos;
    String title, memo, name, date, id, ip;
    LinearLayout linear;
    Button btn;
    TimePicker time;
    Integer y, mon, day;
    DatePicker dday;
    private static String TAG = "phptest";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_boardupload_activity);
        titles = (EditText) findViewById(R.id.txttitle);
        memos = (EditText) findViewById(R.id.txtmemo);
        btn = (Button) findViewById(R.id.btnmake);
        time = (TimePicker) findViewById(R.id.pick);
        dday = (DatePicker) findViewById(R.id.dpick);
        linear = (LinearLayout) findViewById(R.id.linear);
        time.setIs24HourView(true);
        Intent intent = getIntent();
        ip = intent.getStringExtra("IP");
        id = intent.getStringExtra("ID");
        name = intent.getStringExtra("NAME");
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NetworkInfo mNetworkState = getNetworkInfo();
                    if (mNetworkState != null && mNetworkState.isConnected()) {
                        title = titles.getText().toString();
                        memo = memos.getText().toString();
                        if (title.isEmpty() || memo.isEmpty()) throw new Exception();
                        y = dday.getYear();
                        mon = dday.getMonth() + 1;
                        day = dday.getDayOfMonth();
                        date = y.toString() + "-" + mon.toString() + "-" + day.toString();
                        InsertData task = new InsertData();
                        task.execute("http://" + ip + "/board.php", id, name, title, memo, date);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_BoardUpload_Activity.this);
                        builder.setTitle("게시글을 작성하셨습니다");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_BoardUpload_Activity.this);
                        builder.setCancelable(false);
                        builder.setTitle("인터넷 연결을 확인해 주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "값을 모두 입력해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    class InsertData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String idz = (String) params[1];
            String namez = (String) params[2];
            String titlez = (String) params[3];
            String memoz = (String) params[4];
            String datez = (String) params[5];
            String serverURL = (String) params[0];
            String postParameters = "id=" + idz + "&name=" + namez + "&title=" + titlez + "&memo=" + memoz + "&date=" + datez;
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
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }
}