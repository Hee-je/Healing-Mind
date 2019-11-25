package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class In_Content3Select_Activity extends Activity {
    Button straight, anfruf, zigzag;
    String ID, IP, letter, line;
    LinearLayout linear;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_content3select_activity);
        straight = (Button) findViewById(R.id.straight);
        linear = (LinearLayout) findViewById(R.id.linear);
        zigzag = (Button) findViewById(R.id.zigzag);
        anfruf = (Button) findViewById(R.id.anfruf);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        letter = intent.getStringExtra("letter");
        straight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line = "straight";
                Intent intent = new Intent(getApplicationContext(), In_Contents3_Activity.class);
                intent.putExtra("letter", letter);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                intent.putExtra("line", line);
                startActivity(intent);
            }
        });

        zigzag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line = "zigzag";
                Intent intent = new Intent(getApplicationContext(), In_Contents3_Activity.class);
                intent.putExtra("letter", letter);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                intent.putExtra("line", line);
                startActivity(intent);
            }
        });
        anfruf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                line = "anfruf";
                Intent intent = new Intent(getApplicationContext(), In_Contents3_Activity.class);
                intent.putExtra("letter", letter);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                intent.putExtra("line", line);
                startActivity(intent);
            }
        });
    }
}
