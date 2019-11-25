package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class In_Splash_Activity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_splash_activity);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Log.e("에러:", "스플레쉬에러");
        }
        startActivity(new Intent(this, Yun_Login_Activity.class));
        finish();
    }
    public void onBackPressed() {
    }
}
