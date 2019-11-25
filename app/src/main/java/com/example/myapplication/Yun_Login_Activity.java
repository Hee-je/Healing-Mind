package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.LoginButton;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

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

public class Yun_Login_Activity extends AppCompatActivity {
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_ID = "id", TAG_PASSWORD = "password", TAG_NAME = "name", TAG_BIRTH = "birth", TAG_PHONE = "phone", TAG_SEX = "sex", TAG_EMAIL = "email", TAG_FIRST = "first";

    String IP = "";
    String ID, PASSWORD, NAME, BIRTH, PHONE, SEX, EMAIL, FIRST, mJsonString, good_email, good_name, what;
    long good_id;
    EditText mEditTextName1, mEditTextName2;
    Button btn;
    CheckBox auto_login;
    ImageView kakao, naver, fb, google;
    LoginButton login_kakao_real;
    SessionCallback callback;
    boolean auto;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    LinearLayout linear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_login_activity);
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                //로그아웃 성공 후 하고싶은 내용 코딩 ~
            }
        });
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        mEditTextName1 = (EditText) findViewById(R.id.inputID);
        mEditTextName2 = (EditText) findViewById(R.id.inputPASSWORD);
        btn = (Button) findViewById(R.id.loginbtn);
        kakao = (ImageView) findViewById(R.id.kakao);
        naver = (ImageView) findViewById(R.id.naver);
        fb = (ImageView) findViewById(R.id.fb);
        linear = (LinearLayout) findViewById(R.id.linear);
        login_kakao_real = (LoginButton) findViewById(R.id.login_kakao_real);
        auto_login = (CheckBox) findViewById(R.id.auto_login);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        auto = false;
        google = findViewById(R.id.google);
        myHelper = new MyDBHelper(this);
        Cursor cursor;
        sqlDB = myHelper.getReadableDatabase();
        cursor = sqlDB.rawQuery("SELECT *FROM IDcheck WHERE  checked ='" + "1" + "';", null);
        if (cursor.moveToNext()) {
            ID = cursor.getString(1);
            PASSWORD = cursor.getString(2);
            EMAIL = cursor.getString(3);
            NAME = cursor.getString(4);
            cursor.close();
            sqlDB.close();
            NetworkInfo mNetworkState = getNetworkInfo();
            if (mNetworkState != null && mNetworkState.isConnected()) {
                Intent intent = new Intent(getApplicationContext(), In_Frame_Activity.class);
                intent.putExtra("IP", IP);
                intent.putExtra("ID", ID);
                intent.putExtra("EMAIL", EMAIL);
                intent.putExtra("PASSWORD", PASSWORD);
                intent.putExtra("NAME", NAME);
                mEditTextName1.setText("");
                mEditTextName2.setText("");
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Login_Activity.this);
                builder.setCancelable(false);
                builder.setTitle("인터넷 연결을 확인해 주세요");
                builder.setPositiveButton("확인", null);
                builder.create().show();
            }
        }
        cursor.close();
        sqlDB.close();

        kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_kakao_real.performClick();
            }
        });
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auto_login.isChecked()) {
                    auto = true;
                } else {
                    auto = false;
                }
                Intent n = new Intent(getApplicationContext(), In_Google_Activity.class);
                n.putExtra("IP", IP);
                n.putExtra("auto", auto);
                mEditTextName1.setText("");
                mEditTextName2.setText("");
                startActivity(n);
            }
        });
        naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auto_login.isChecked()) {
                    auto = true;
                } else {
                    auto = false;
                }
                Intent n = new Intent(getApplicationContext(), Je_Naver_Activity.class);
                n.putExtra("IP", IP);
                n.putExtra("auto", auto);
                mEditTextName1.setText("");
                mEditTextName2.setText("");
                startActivity(n);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auto_login.isChecked()) {
                    auto = true;
                } else {
                    auto = false;
                }
                Intent f = new Intent(getApplicationContext(), Yun_FB_Activity.class);
                f.putExtra("IP", IP);
                f.putExtra("auto", auto);
                mEditTextName1.setText("");
                mEditTextName2.setText("");
                startActivity(f);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ID = mEditTextName1.getText().toString();
                    PASSWORD = mEditTextName2.getText().toString();
                    if (ID.isEmpty() || PASSWORD.isEmpty()) throw new Exception();
                    GetData task = new GetData();
                    task.execute(mEditTextName1.getText().toString(), mEditTextName2.getText().toString());
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Login_Activity.this);
                    builder.setTitle("아이디 및 비밀번호를 확인해 주세요");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
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

    public void serch(View v) {
        Intent intent = new Intent(getApplicationContext(), Je_All_Search_Activity.class);
        intent.putExtra("IP", IP);
        startActivity(intent);
    }

    public void member(View v) {
        Intent intent = new Intent(getApplicationContext(), Yun_Makeuser_Activity.class);
        intent.putExtra("IP", IP);
        startActivity(intent);
    }

    class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_Login_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result != null) {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://" + IP + "/login.php";
            String postParameters = "id=" + searchKeyword1 + "&password=" + searchKeyword2;
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

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            if (jsonArray.length() == 0) throw new Exception();
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

            if (auto_login.isChecked()) {
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
            NetworkInfo mNetworkState = getNetworkInfo();
            if(mNetworkState != null && mNetworkState.isConnected()) {
                Intent intent = new Intent(getApplicationContext(), In_Frame_Activity.class);
                intent.putExtra("IP", IP);
                intent.putExtra("ID", ID);
                intent.putExtra("PASSWORD", PASSWORD);
                intent.putExtra("EMAIL", EMAIL);
                intent.putExtra("NAME", NAME);
                mEditTextName1.setText("");
                mEditTextName2.setText("");
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Login_Activity.this);
                builder.setCancelable(false);
                builder.setTitle("인터넷 연결을 확인해 주세요");
                builder.setPositiveButton("확인",null);
                builder.create().show();
            }
        } catch (Exception e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Login_Activity.this);
            builder.setTitle("아이디 및 비밀번호를 확인해 주세요");
            builder.setPositiveButton("확인", null);
            builder.create().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);
                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    if (userProfile != null) {
                        userProfile.saveUserToCache();
                    }
                    Logger.e("succeeded to update user profile", userProfile, "\n");
                    good_email = userProfile.getEmail();
                    good_name = userProfile.getNickname();//닉네임
                    good_id = userProfile.getId();//사용자 고유번호
                    what = "카카오";
                    final String pImage = userProfile.getProfileImagePath();//사용자 프로필 경로
                    Log.e("UserProfile", userProfile.toString());//전체 정보 출력
                    GetData2 task_kakao = new GetData2();
                    task_kakao.execute(Long.toString(good_id), Long.toString(good_id));
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Logger.e(exception);
            }
        }
    }

    class GetData2 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_Login_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result != null) {
                mJsonString = result;
                showResult2();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://" + IP + "/login_main.php";
            String postParameters = "id=" + searchKeyword1 + "&password=" + searchKeyword2;
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

            if (auto_login.isChecked()) {
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
            NetworkInfo mNetworkState = getNetworkInfo();
            if(mNetworkState != null && mNetworkState.isConnected()) {
                Intent intent = new Intent(getApplicationContext(), In_Frame_Activity.class);
                intent.putExtra("IP", IP);
                intent.putExtra("ID", ID);
                intent.putExtra("EMAIL", EMAIL);
                intent.putExtra("PASSWORD", PASSWORD);
                intent.putExtra("NAME", NAME);
                mEditTextName1.setText("");
                mEditTextName2.setText("");
                startActivity(intent);
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Login_Activity.this);
                builder.setCancelable(false);
                builder.setTitle("인터넷 연결을 확인해 주세요");
                builder.setPositiveButton("확인",null);
                builder.create().show();
            }
        } catch (JSONException e) {
            Intent intent = new Intent(getApplicationContext(), Yun_Idcheck_Activity.class);
            intent.putExtra("IP", IP);
            intent.putExtra("ID", Long.toString(good_id));
            intent.putExtra("EMAIL", good_email);
            intent.putExtra("NAME", good_name);
            intent.putExtra("PASSWORD", Long.toString(good_id));
            intent.putExtra("what", what);
            mEditTextName1.setText("");
            mEditTextName2.setText("");
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
    }
}