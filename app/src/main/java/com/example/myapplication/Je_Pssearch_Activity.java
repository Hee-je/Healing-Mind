package com.example.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Je_Pssearch_Activity extends AppCompatActivity {
    private static final String TAG = "phpquerytest";
    String IP, ID, mJsonString;
    EditText ps_first, ps_second;
    LinearLayout linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_pssearch_activity);
        ps_first = (EditText) findViewById(R.id.ps_first);
        ps_second = (EditText) findViewById(R.id.ps_second);
        linear = (LinearLayout) findViewById(R.id.linear);
        Button change = (Button) findViewById(R.id.change);
        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        ID = intent.getStringExtra("id");
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ps_first.getText().toString().equals(ps_second.getText().toString())) {
                        Je_Pssearch_Activity.GetData task = new Je_Pssearch_Activity.GetData();
                        task.execute(ID, ps_first.getText().toString(), ps_second.getText().toString());
                        AlertDialog.Builder builder = new AlertDialog.Builder(Je_Pssearch_Activity.this);
                        builder.setTitle("비밀번호가 변경되었습니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                        builder.create().show();
                    } else {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Je_Pssearch_Activity.this);
                    builder.setTitle("비밀번호를 확인해 주세요");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }
            }
        });
    }

    class GetData extends AsyncTask<String, Void, String> {
        String errorString = null;
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
            String id = params[0];
            String newpassword = params[1];
            String checkpassworod = params[2];
            String serverURL = "http://" + IP + "/update_pw.php";
            String postParameters = "&id=" + id + "&newpassword=" + newpassword + "&checkpassword=" + checkpassworod;
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