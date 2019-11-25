package com.example.myapplication;

import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Je_Recently_Activity extends AppCompatActivity {
    ListView listView;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String IP, ID, mJsonString, title, letter;
    LinearLayout linear;
    HashMap<String, String> mapData;
    ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
    String[][] num = new String[3][20];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_recently_activity);
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        Intent intent2 = getIntent();
        IP = intent2.getStringExtra("IP");
        ID = intent2.getStringExtra("ID");
        listView = (ListView) findViewById(R.id.listView);
        myHelper = new MyDBHelper(this);
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "';", null);
        Integer i = 0;
        while (cursor.moveToNext()) {
            num[0][i] = cursor.getString(2);
            Integer img_IN = getResources().getIdentifier(num[0][i], null, getPackageName());
            num[1][i] = cursor.getString(3);
            num[2][i] = cursor.getString(4);
            mapData = new HashMap<String, String>();
            mapData.put("img", img_IN.toString());
            mapData.put("title", num[1][i]);
            mapData.put("letter", num[2][i]);
            datalist.add(mapData);
            i++;
        }
        ListAdapter adapter = new SimpleAdapter(
                this, datalist, R.layout.yun_contents_activity,
                new String[]{"img", "title", "letter"},
                new int[]{R.id.text_img, R.id.text_title, R.id.text_letter}
        );
        listView.setAdapter(adapter);
        cursor.close();
        sqlDB.close();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                letter = num[2][position];
                if (num[1][position].equals("5분명상")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_5minutes_Activity.class);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("발성")) {
                    Intent intent = new Intent(getApplicationContext(), Je_Vocalization_Activity.class);
                    intent.putExtra("letter", letter);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("시지각")) {
                    Intent intent = new Intent(getApplicationContext(), In_VisualPerception_Activity.class);
                    intent.putExtra("letter", letter);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("그리기 - 직선")) {
                    Intent intent = new Intent(getApplicationContext(), In_Contents3_Activity.class);
                    intent.putExtra("letter", letter);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    intent.putExtra("line", "straight");
                    startActivity(intent);
                } else if (num[1][position].equals("그리기 - 지그재그")) {
                    Intent intent = new Intent(getApplicationContext(), In_Contents3_Activity.class);
                    intent.putExtra("letter", letter);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    intent.putExtra("line", "zigzag");
                    startActivity(intent);
                } else if (num[1][position].equals("그리기 - 물결")) {
                    Intent intent = new Intent(getApplicationContext(), In_Contents3_Activity.class);
                    intent.putExtra("letter", letter);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    intent.putExtra("line", "anfruf");
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("비")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "rain");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("바다")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "sea");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("눈")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "snow");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("계곡")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "valley");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("산")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "mountain");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("종")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "bell");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                } else if (num[1][position].equals("소리") && letter.equals("장작")) {
                    Intent intent = new Intent(getApplicationContext(), Yun_Contents4_Activity.class);
                    intent.putExtra("category_k", letter);
                    intent.putExtra("category", "wood");
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                }
            }
        });
    }
}