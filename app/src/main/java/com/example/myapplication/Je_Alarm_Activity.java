package com.example.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class Je_Alarm_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String ID;
    ListView alarm;
    String a, b, c, d, e, f, g;
    LinearLayout linear;
    String[][] num = new String[9][50];
    ArrayList<HashMap<String, String>> datalist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> mapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_alarmtool_activity);
        Intent intent = getIntent();
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        ID = intent.getStringExtra("ID");
        alarm = findViewById(R.id.alarm);
        myHelper = new MyDBHelper(this);
        Button btn = (Button) findViewById(R.id.btn);
        change();
        SimpleAdapter adapter = new SimpleAdapter(this, datalist, android.R.layout.simple_list_item_2, new String[]{"time", "week"}, new int[]{android.R.id.text1, android.R.id.text2});
        alarm.setAdapter(adapter);
        alarm.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("Tag", ":" + position);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Je_Alarm_Activity.this);
                alertDialogBuilder.setTitle("삭제");
                alertDialogBuilder
                        .setMessage("삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("네",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sqlDB = myHelper.getWritableDatabase();
                                        sqlDB.execSQL("DELETE FROM alarm WHERE _id =  '" + ID + "' AND hour = '" + num[0][position] + "' AND minute = '" + num[1][position] + "'AND mon = '" + num[2][position] + "'AND tue = '" + num[3][position] + "'AND wed = '" + num[4][position] + "'AND thu = '" + num[5][position] + "'AND fri = '" + num[6][position] + "'AND sat = '" + num[7][position] + "'AND sun = '" + num[8][position] + "'");
                                        datalist.clear();
                                        change();
                                        alarm.setAdapter(adapter);
                                        sqlDB.close();
                                    }
                                })
                        .setNegativeButton("아니오",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return true;
            }
        });
        alarm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), Je_Alarmupdate_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("hour", num[0][position]);
                intent.putExtra("minute", num[1][position]);
                intent.putExtra("a", num[2][position]);
                intent.putExtra("b", num[3][position]);
                intent.putExtra("c", num[4][position]);
                intent.putExtra("d", num[5][position]);
                intent.putExtra("e", num[6][position]);
                intent.putExtra("f", num[7][position]);
                intent.putExtra("g", num[8][position]);
                startActivity(intent);
                finish();
            }
        });
        FloatingActionButton plus = (FloatingActionButton) findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Je_Alarmset_Activity.class);
                intent.putExtra("ID", ID);
                startActivity(intent);
                finish();
            }
        });
    }

    void change() {
        Integer i = 0;
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor = sqlDB.rawQuery("SELECT *FROM alarm WHERE _id = '" + ID + "';", null);
        while (cursor.moveToNext()) {
            num[0][i] = cursor.getString(2);
            num[1][i] = cursor.getString(3);
            num[2][i] = cursor.getString(4);
            num[3][i] = cursor.getString(5);
            num[4][i] = cursor.getString(6);
            num[5][i] = cursor.getString(7);
            num[6][i] = cursor.getString(8);
            num[7][i] = cursor.getString(9);
            num[8][i] = cursor.getString(10);
            if (num[2][i].equals("1")) a = "월 ";
            else a = "";
            if (num[3][i].equals("1")) b = "화 ";
            else b = "";
            if (num[4][i].equals("1")) c = "수 ";
            else c = "";
            if (num[5][i].equals("1")) d = "목 ";
            else d = "";
            if (num[6][i].equals("1")) e = "금 ";
            else e = "";
            if (num[7][i].equals("1")) f = "토 ";
            else f = "";
            if (num[8][i].equals("1")) g = "일 ";
            else g = "";
            mapData = new HashMap<String, String>();
            mapData.put("time", num[0][i] + "시 " + num[1][i] + "분");
            mapData.put("week", a + b + c + d + e + f + g);
            datalist.add(mapData);
            i++;
        }
        cursor.close();
        sqlDB.close();
    }
}