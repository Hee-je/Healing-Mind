
package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Je_Naver2_Activity extends AppCompatActivity {
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_ID = "id", TAG_PASSWORD = "password", TAG_NAME = "name", TAG_BIRTH = "birth", TAG_PHONE = "phone", TAG_SEX = "sex", TAG_EMAIL = "email", TAG_FIRST = "first";
    String ID, PASSWORD, NAME, BIRTH, PHONE, SEX, EMAIL, FIRST, mJsonString, what, IP;
    boolean auto = false;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent2 = getIntent();
        IP = intent2.getStringExtra("IP");
        ID = intent2.getStringExtra("ID");
        EMAIL = intent2.getStringExtra("EMAIL");
        NAME = intent2.getStringExtra("NAME");
        what = intent2.getStringExtra("what");
        auto = intent2.getBooleanExtra("auto", auto);
        GetData2 task_n = new GetData2();
        task_n.execute();
    }

    class GetData2 extends AsyncTask<String, Void, String> {
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
                showResult2();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://" + IP + "/login_main.php";
            String postParameters = "id=" + ID + "&password=" + ID;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString().trim();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult2() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                ID = item.getString(TAG_ID);
                PASSWORD = item.getString(TAG_PASSWORD);
                NAME = item.getString(TAG_NAME);
                BIRTH = item.getString(TAG_BIRTH);
                PHONE = item.getString(TAG_PHONE);
                SEX = item.getString(TAG_SEX);
                EMAIL = item.getString(TAG_EMAIL);
                FIRST = item.getString(TAG_FIRST);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, ID);
                hashMap.put(TAG_PASSWORD, PASSWORD);
                hashMap.put(TAG_NAME, NAME);
                hashMap.put(TAG_BIRTH, BIRTH);
                hashMap.put(TAG_PHONE, PHONE);
                hashMap.put(TAG_SEX, SEX);
                hashMap.put(TAG_EMAIL, EMAIL);
                hashMap.put(TAG_FIRST, FIRST);
            }

            if (auto) {
                myHelper = new MyDBHelper(this);
                Cursor login_cursor;
                sqlDB = myHelper.getReadableDatabase();
                login_cursor = sqlDB.rawQuery("SELECT *FROM IDcheck WHERE  checked ='" + "1" + "';", null);
                if (!login_cursor.moveToNext()) {
                    sqlDB = myHelper.getWritableDatabase();
                    sqlDB.execSQL("INSERT INTO IDcheck VALUES (NULL, '" + ID + "','" + PASSWORD + "','" + EMAIL + "','" + NAME + "','" + "1" + "');");
                }
                login_cursor.close();
                sqlDB.close();
            }

            Intent intent = new Intent(getApplicationContext(), In_Frame_Activity.class);
            intent.putExtra("IP", IP);
            intent.putExtra("ID", ID);
            intent.putExtra("EMAIL", EMAIL);
            intent.putExtra("PASSWORD", PASSWORD);
            intent.putExtra("NAME", NAME);
            startActivity(intent);
            finish();
        } catch (JSONException e) {
            Intent intent = new Intent(getApplicationContext(), Yun_Idcheck_Activity.class);
            intent.putExtra("IP", IP);
            intent.putExtra("ID", ID);
            intent.putExtra("EMAIL", EMAIL);
            intent.putExtra("NAME", NAME);
            intent.putExtra("PASSWORD", ID);
            intent.putExtra("what", what);
            startActivity(intent);
            finish();
        }
    }
}