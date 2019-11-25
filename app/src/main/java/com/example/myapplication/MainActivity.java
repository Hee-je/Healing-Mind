package com.example.myapplication;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.net.URLConnection;
import java.net.URL;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ImageView[] numTesx = new ImageView[5];
    Integer[] numTextIds = {R.id.img1,R.id.img2,R.id.img3,R.id.img4,R.id.img5};
    Integer img_count = 1, end = 5;
    String ID, IP, category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        category = intent.getStringExtra("category");
        if(category.equals("snow")) { end = 2; }
        for ( int i = 0; i < numTextIds.length; i++)
        {
            numTesx[i] =  (ImageView)findViewById(numTextIds[i]);
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // 네트워크 에러나는거 방지
        StrictMode.setThreadPolicy(policy); // 네트워크 에러나는거 방지
        try {

            for(int i = 0; i < end; i++) {
                final int index;
                index = i;
                URL url = new URL("http://" + IP + "/contents/"+category+"/"+category+"_"+img_count.toString());
                URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                Bitmap bm = BitmapFactory.decodeStream(bis);
                bis.close();
                numTesx[index].setVisibility(View.VISIBLE);
                numTesx[index].setImageBitmap(bm);
                img_count++;
            }
        } catch (Exception e) { Toast.makeText(getApplicationContext(),e+"",Toast.LENGTH_SHORT).show(); }



        for ( int i = 0; i < numTextIds.length; i++)
        {
            final int index;
            index = i;
            numTesx[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(),category+" " +index + " 이다",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}