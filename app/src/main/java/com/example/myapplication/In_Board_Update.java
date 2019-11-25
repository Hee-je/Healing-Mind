package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class In_Board_Update extends AppCompatActivity {
    private static final String TAG = "phpquerytest";
    EditText memo, title;
    LinearLayout linear;
    Button update;
    String check, ID, IP, NAME, NUM, DATE, mJsonString;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_board_update);
        title = (EditText) findViewById(R.id.txttitle);
        memo = (EditText) findViewById(R.id.txtmemo);
        update = (Button) findViewById(R.id.btnupdate);
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        final Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        memo.setText(intent.getStringExtra("MEMO"));
        title.setText(intent.getStringExtra("TITLE"));
        NUM = intent.getStringExtra("NUM");
        DATE = intent.getStringExtra("DATE");
        check = intent.getStringExtra("CHECK");
        NAME = intent.getStringExtra("NAME");
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    NetworkInfo mNetworkState = getNetworkInfo();
                    if (mNetworkState != null && mNetworkState.isConnected()) {
                        In_Board_Update.GetData task = new In_Board_Update.GetData();
                        task.execute(NUM, title.getText().toString(), memo.getText().toString());
                        Intent show = new Intent(getApplicationContext(), Yun_ShowBoard_Activity.class);
                        show.putExtra("TITLE", title.getText().toString());
                        show.putExtra("DATE", DATE);
                        show.putExtra("MEMO", memo.getText().toString());
                        show.putExtra("board_NAME", NAME);
                        show.putExtra("ID", ID);
                        show.putExtra("IP", IP);
                        show.putExtra("NUM", NUM);
                        show.putExtra("CHECK", check);
                        show.putExtra("UPDATE", "update");
                        startActivity(show);
                        finish();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(In_Board_Update.this);
                        builder.setCancelable(false);
                        builder.setTitle("인터넷 연결을 확인해 주세요");
                        builder.setPositiveButton("확인",null);
                        builder.create().show();
                    }
                } catch (Exception e) {
                    Log.d(TAG, "UpdateError: Error ", e);
                    finish();
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

    class GetData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "response - " + result);
            if (result != null) {
                mJsonString = result;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String num = params[0];
            String title = params[1];
            String memo = params[2];
            String serverURL = "http://" + IP + "/update_memo.php";
            String postParameters = "&num=" + num + "&title=" + title + "&memo=" + memo;
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
