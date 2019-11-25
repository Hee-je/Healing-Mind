package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;

public class MyDBHelper extends SQLiteOpenHelper {
    public MyDBHelper(Context context) {
        super(context, "groupdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE calendar(num TEXT PRIMARY KEY, _id TEXT, year_month_day TEXT, schedule TEXT)");
        db.execSQL("CREATE TABLE attendance(num TEXT PRIMARY KEY, _id TEXT, year TEXT, month TEXT, day TEXT)");
        db.execSQL("CREATE TABLE alarm(num TEXT PRIMARY KEY, _id TEXT, hour TEXT, minute TEXT, mon TEXT, tue TEXT, wed TEXT, thu TEXT, fri TEXT, sat TEXT, sun TEXT,body TEXT)");
        db.execSQL("CREATE TABLE vocalization(num TEXT PRIMARY KEY, _id TEXT, letter TEXT, step1 TEXT, step2 TEXT, step3 TEXT, step4 TEXT, step5 TEXT, step6 TEXT)");
        db.execSQL("CREATE TABLE visualperception(num TEXT PRIMARY KEY, _id TEXT, letter TEXT, seekbar TEXT)");
        db.execSQL("CREATE TABLE recently(num TEXT PRIMARY KEY, _id TEXT, img TEXT, title TEXT, letter TEXT)");
        db.execSQL("CREATE TABLE bookmark(num TEXT PRIMARY KEY, _id TEXT, img TEXT, title TEXT, letter TEXT)");
        db.execSQL("CREATE TABLE IDcheck(num TEXT PRIMARY KEY, _id TEXT, password TEXT, email TEXT, name TEXT, checked TEXT)");
        db.execSQL("CREATE TABLE notices(num INTEGER PRIMARY KEY)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS calender");
        db.execSQL("DROP TABLE IF EXISTS attendance");
        db.execSQL("DROP TABLE IF EXISTS alarm");
        db.execSQL("DROP TABLE IF EXISTS vocalization");
        db.execSQL("DROP TABLE IF EXISTS visualperception");
        db.execSQL("DROP TABLE IF EXISTS bookmark");
        db.execSQL("DROP TABLE IF EXISTS IDcheck");
        db.execSQL("DROP TABLE IF EXISTS notices");
        onCreate(db);
    }
}