package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class Yun_FB_Activity extends AppCompatActivity {
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_ID = "id", TAG_PASSWORD = "password", TAG_NAME = "name", TAG_BIRTH = "birth", TAG_PHONE = "phone", TAG_SEX = "sex", TAG_EMAIL = "email", TAG_FIRST = "first";
    private CallbackManager callbackManager;
    String NAME, EMAIL, IP, mJsonString, PASSWORD, BIRTH, PHONE, SEX, FIRST, what, ID;
    Integer id;
    LoginButton loginButton;
    boolean auto;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_fb_activity);
        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        auto = intent.getBooleanExtra("auto", auto);
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.setLoginBehavior(LoginBehavior.WEB_ONLY);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        Log.v("결과 ", user.toString());
                        try {
                            ID = user.getString("id");
                            NAME = user.getString("name");
                            what = "페이스북";
                            GetData2 task_fb = new GetData2();
                            task_fb.execute();
                        } catch (Exception e) {
                            finish();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {
                finish();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("LoginErr", error.toString());
            }
        });
        loginButton.performClick();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
            intent.putExtra("NAME", NAME);
            intent.putExtra("PASSWORD", ID);
            intent.putExtra("what", what);
            startActivity(intent);
            finish();
        }
    }
}