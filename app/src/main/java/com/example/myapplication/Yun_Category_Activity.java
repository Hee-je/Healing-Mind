package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class Yun_Category_Activity extends AppCompatActivity {

    LinearLayout linear;
    Button[] numButtons = new Button[8];
    Integer[] numBtnIds = {R.id.rain_btn, R.id.sea_btn, R.id.sonw_btn, R.id.wood_btn, R.id.valley_btn, R.id.mountain_btn, R.id.bell_btn, R.id.heart_btn};
    String ID, IP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_category_activity);
        Intent intent_category = getIntent();
        IP = intent_category.getStringExtra("IP");
        ID = intent_category.getStringExtra("ID");
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        for (int i = 0; i < numBtnIds.length; i++) {
            numButtons[i] = (Button) findViewById(numBtnIds[i]);
        }
        for (int i = 0; i < numBtnIds.length; i++) {
            final int index;
            index = i;
            numButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    intent.putExtra("category", numButtons[index].getTag().toString());
                    intent.putExtra("category_k", numButtons[index].getText().toString());
                    startActivity(intent);
                }
            });
        }
    }
}