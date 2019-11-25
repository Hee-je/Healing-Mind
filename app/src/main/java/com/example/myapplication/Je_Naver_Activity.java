package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;

import org.json.JSONObject;

public class Je_Naver_Activity extends AppCompatActivity {
    boolean auto;
    String ID, NAME, EMAIL, mJsonString, what, IP;
    public static OAuthLogin mOAuthLoginModule;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent2 = getIntent();
        IP = intent2.getStringExtra("IP");
        auto = intent2.getBooleanExtra("auto", auto);
        mOAuthLoginModule = OAuthLogin.getInstance();
        mOAuthLoginModule.init(
                Je_Naver_Activity.this,
                "Vua9gwh1gWjDECOaSvuz",
                "oYrNeUrXq8",
                "Healing Mind"
        );
        mOAuthLoginModule.startOauthLoginActivity(Je_Naver_Activity.this, mOAuthLoginHandler);
    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginModule.getAccessToken(Je_Naver_Activity.this);
                new Thread() {
                    public void run() {
                        String data = mOAuthLoginModule.requestApi(Je_Naver_Activity.this, accessToken, "https://openapi.naver.com/v1/nid/me");
                        try {
                            what = "네이버";
                            JSONObject result = new JSONObject(data);
                            NAME = result.getJSONObject("response").getString("name");
                            EMAIL = result.getJSONObject("response").getString("email");
                            ID = result.getJSONObject("response").getString("id");
                            Intent intent = new Intent(getApplicationContext(), Je_Naver2_Activity.class);
                            intent.putExtra("IP", IP);
                            intent.putExtra("ID", ID);
                            intent.putExtra("EMAIL", EMAIL);
                            intent.putExtra("NAME", NAME);
                            intent.putExtra("what", what);
                            intent.putExtra("auto", auto);
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                        }
                    }
                }.start();
            } else {
                String errorCode = mOAuthLoginModule.getLastErrorCode(Je_Naver_Activity.this).getCode();
                String errorDesc = mOAuthLoginModule.getLastErrorDesc(Je_Naver_Activity.this);
                Toast.makeText(Je_Naver_Activity.this, "errorCode:" + errorCode
                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        }

        ;
    };
}
