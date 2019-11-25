package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class In_Frame_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String ID, IP, NAME, BIRTH, SEX, PHONE, EMAIL, FIRST, THEMA, RECENTLY, mJsonString, PASSWORD, N_NUM;
    NavigationView navigationView;
    boolean first = true;
    boolean NoTice = false;
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_THEMA = "thema", TAG_NAME = "name", TAG_BIRTH = "birth", TAG_PHONE = "phone", TAG_SEX = "sex", TAG_FIRST = "first", TAG_RECENTLY = "recently", TAG_NUM = "num", TAG_TITLE = "title", TAG_DATE = "date", TAG_NOTICE = "notice";
    Bitmap getBlob;
    Date day1, day2;
    ImageView image;
    ActionBarDrawerToggle actionToggle;
    Integer y, m, d;
    TextView email_tool, nav_id;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private In_Fragment_Main F_Main = new In_Fragment_Main();
    private Je_Fragment_Calendar F_Calendar = new Je_Fragment_Calendar();
    private Yun_Fragment_Board F_Board = new Yun_Fragment_Board();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_toolbar);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomB);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame, F_Main).commitAllowingStateLoss();
        try {
            LoginManager.getInstance().logOut();
        } catch (Exception e) {
        }
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.main:
                        transaction.replace(R.id.frame, F_Main).commitAllowingStateLoss();
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        return true;
                    case R.id.calendar:
                        transaction.replace(R.id.frame, F_Calendar).commitAllowingStateLoss();
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        return true;
                    case R.id.board:
                        transaction.replace(R.id.frame, F_Board).commitAllowingStateLoss();
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        return true;
                }
                return false;
            }
        });
        Intent get_intent = getIntent();
        IP = get_intent.getStringExtra("IP");
        NAME = get_intent.getStringExtra("NAME");
        ID = get_intent.getStringExtra("ID");
        EMAIL = get_intent.getStringExtra("EMAIL");
        PASSWORD = get_intent.getStringExtra("PASSWORD");
        try {
            if (IP == null) {
                Intent gologin = new Intent(getApplicationContext(), Yun_Login_Activity.class);
                startActivity(gologin);
                finish();
            }
        } catch (Exception e) {
        }
        GetData1 task = new GetData1();
        task.execute(ID, PASSWORD);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
            }
        }, 2000);
        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH) + 1;
        d = calendar.get(Calendar.DAY_OF_MONTH);
        myHelper = new MyDBHelper(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }
        actionToggle = new ActionBarDrawerToggle(this, drawer, R.string.app_name, R.string.app_name);
        drawer.addDrawerListener(actionToggle);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View nav_header_view = navigationView.getHeaderView(0);
        nav_id = (TextView) nav_header_view.findViewById(R.id.name_set);
        email_tool = (TextView) nav_header_view.findViewById(R.id.textView);
        image = (ImageView) nav_header_view.findViewById(R.id.imageView);
        nav_id.setText(NAME + "님 반갑습니다.");
        email_tool.setText(EMAIL);
        GetData tasks = new GetData();
        GetData_for_Notice tasks1 = new GetData_for_Notice();
        tasks1.execute();
        tasks.execute(ID);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionToggle.syncState();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_my) {
            Intent my = new Intent(getApplicationContext(), In_Person_Update_Activity.class);
            my.putExtra("ID", ID);
            my.putExtra("IP", IP);
            my.putExtra("PASSWORD", PASSWORD);
            my.putExtra("NAME", NAME);
            my.putExtra("BIRTH", BIRTH);
            my.putExtra("SEX", SEX);
            my.putExtra("PHONE", PHONE);
            my.putExtra("EMAIL", EMAIL);
            my.putExtra("FIRST", FIRST);
            startActivity(my);
        } else if (id == R.id.nav_alarm) {
            Intent intent = new Intent(getApplicationContext(), Je_Alarm_Activity.class);
            intent.putExtra("ID", ID);
            startActivity(intent);
        } else if (id == R.id.nav_chart) {
            Intent intent = new Intent(getApplicationContext(), Je_Chart_Activity.class);
            intent.putExtra("ID", ID);
            intent.putExtra("IP", IP);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            Cursor delete_cursor;
            sqlDB = myHelper.getReadableDatabase();
            delete_cursor = sqlDB.rawQuery("SELECT *FROM IDcheck WHERE  checked = '" + "1" + "';", null);
            if (delete_cursor.moveToNext()) {
                sqlDB = myHelper.getWritableDatabase();
                sqlDB.execSQL("DELETE FROM IDcheck WHERE checked = '" + "1" + "'");
            }
            delete_cursor.close();
            sqlDB.close();
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    finish();
                }
            });
        } else if (id == R.id.nav_notice) {
            Intent intent = new Intent(getApplicationContext(), In_Notice_Activity.class);
            intent.putExtra("ID", ID);
            intent.putExtra("IP", IP);
            startActivity(intent);
        } else if (id == R.id.nav_del) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("탈퇴하시겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sqlDB = myHelper.getReadableDatabase();
                            Cursor delete_cursor;
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM calendar WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM attendance WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM attendance WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM alarm WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM alarm WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM vocalization WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM vocalization WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM visualperception WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM visualperception WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM recently WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM recently WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM bookmark WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM bookmark WHERE _id = '" + ID + "'");
                            }
                            sqlDB = myHelper.getReadableDatabase();
                            delete_cursor = sqlDB.rawQuery("SELECT *FROM IDcheck WHERE _id = '" + ID + "';", null);
                            while (delete_cursor.moveToNext()) {
                                sqlDB = myHelper.getWritableDatabase();
                                sqlDB.execSQL("DELETE FROM IDcheck WHERE _id = '" + ID + "'");
                            }
                            delete_cursor.close();
                            sqlDB.close(); // 내부DB삭제
                            DeleteData Person_task = new DeleteData();
                            Person_task.execute("http://" + IP + "/delete_person.php", ID);
                            DeleteData2 Images_task = new DeleteData2();
                            Images_task.execute("http://" + IP + "/delete_images.php", ID);
                            finish();
                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        } else if (id == R.id.nav_QnA) {
            Intent intent = new Intent(getApplicationContext(), In_QnA_Activity.class);
            intent.putExtra("IP", IP);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onClickUnlink() {//앱 탈퇴 처리
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                        Toast.makeText(getApplicationContext(), "탈퇴되었습니다.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), Yun_Login_Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    class GetData extends AsyncTask<String, Void, String> {
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
                try {
                    byte[] bImage = Base64.decode(result, 0);
                    ByteArrayInputStream bais = new ByteArrayInputStream(bImage);
                    getBlob = BitmapFactory.decodeStream(bais);
                    if (getBlob != null) {
                        image.setImageBitmap(getBlob);
                    } else {
                        image.setImageResource(R.mipmap.rest);
                    }
                } catch (Exception e) {
                    finish();
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String serverURL = "http://" + IP + "/image_print.php";
            String postParameters = "&id=" + id;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "UpdateData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    class GetData1 extends AsyncTask<String, Void, String> {
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
                showResult();
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

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                NAME = item.getString(TAG_NAME);
                BIRTH = item.getString(TAG_BIRTH);
                PHONE = item.getString(TAG_PHONE);
                SEX = item.getString(TAG_SEX);
                FIRST = item.getString(TAG_FIRST);
                RECENTLY = item.getString(TAG_RECENTLY);
                THEMA = item.getString(TAG_THEMA);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_NAME, NAME);
                hashMap.put(TAG_BIRTH, BIRTH);
                hashMap.put(TAG_PHONE, PHONE);
                hashMap.put(TAG_SEX, SEX);
                hashMap.put(TAG_FIRST, FIRST);
                hashMap.put(TAG_RECENTLY, RECENTLY);
                hashMap.put(TAG_THEMA, THEMA);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    day1 = format.parse(y.toString() + "-" + m.toString() + "-" + d.toString());
                    day2 = format.parse(RECENTLY);
                    if (day1.compareTo(day2) > 0) {
                        GetData_day task_day = new GetData_day();
                        task_day.execute(ID);
                    }
                } catch (Exception e) {
                }
            }
        } catch (JSONException e) {
        }
    }

    class GetData_day extends AsyncTask<String, Void, String> {
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
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String days = y + "-" + m + "-" + d;
            Integer first = Integer.parseInt(FIRST) + 1;
            RECENTLY = days;
            String serverURL = "http://" + IP + "/update_day.php";
            String postParameters = "&id=" + id + "&recently=" + days + "&first=" + first.toString();
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "UpdateData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GetData task_img = new GetData();
        task_img.execute(ID);
        GetData1 task = new GetData1();
        GetData_for_Notice task1 = new GetData_for_Notice();
        task1.execute();
        task.execute(ID, PASSWORD);
        nav_id.setText(NAME + "님 반갑습니다.");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH) + 1;
        d = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            day1 = format.parse(y.toString() + "-" + m.toString() + "-" + d.toString());
            day2 = format.parse(RECENTLY);
            if (day1.compareTo(day2) > 0) {
                GetData_day task_day = new GetData_day();
                task_day.execute(ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetData task_img = new GetData();
        task_img.execute(ID);
        GetData1 task = new GetData1();
        GetData_for_Notice task1 = new GetData_for_Notice();
        task1.execute();
        task.execute(ID, PASSWORD);
        nav_id.setText(NAME + "님 반갑습니다.");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH) + 1;
        d = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            day1 = format.parse(y.toString() + "-" + m.toString() + "-" + d.toString());
            day2 = format.parse(RECENTLY);
            if (day1.compareTo(day2) > 0) {
                GetData_day task_day = new GetData_day();
                task_day.execute(ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        GetData task_img = new GetData();
        task_img.execute(ID);
        GetData1 task = new GetData1();
        GetData_for_Notice task1 = new GetData_for_Notice();
        task1.execute();
        task.execute(ID, PASSWORD);
        nav_id.setText(NAME + "님 반갑습니다.");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH) + 1;
        d = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            day1 = format.parse(y.toString() + "-" + m.toString() + "-" + d.toString());
            day2 = format.parse(RECENTLY);
            if (day1.compareTo(day2) > 0) {
                GetData_day task_day = new GetData_day();
                task_day.execute(ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        GetData task_img = new GetData();
        task_img.execute(ID);
        GetData1 task = new GetData1();
        task.execute(ID, PASSWORD);
        GetData_for_Notice task1 = new GetData_for_Notice();
        task1.execute();
        nav_id.setText(NAME + "님 반갑습니다.");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONTH) + 1;
        d = calendar.get(Calendar.DAY_OF_MONTH);
        try {
            day1 = format.parse(y.toString() + "-" + m.toString() + "-" + d.toString());
            day2 = format.parse(RECENTLY);
            if (day1.compareTo(day2) > 0) {
                GetData_day task_day = new GetData_day();
                task_day.execute(ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class GetData_for_Notice extends AsyncTask<String, Void, String> {
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
                showResult_N();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://" + IP + "/Notice_maxnum.php";
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
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

        private void showResult_N() {
            try {
                JSONObject jsonObject = new JSONObject(mJsonString);
                JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject item = jsonArray.getJSONObject(i);
                    N_NUM = item.getString(TAG_NUM);
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(TAG_NUM, N_NUM);
                    Log.d(TAG, "showResult_N : " + N_NUM);
                    if (!N_NUM.equals(null)) {
                        sqlDB = myHelper.getReadableDatabase();
                        Cursor cursor1 = sqlDB.rawQuery("SELECT * FROM notices;", null);
                        if (!cursor1.moveToNext()) {
                            NoTice = false;
                        } else {
                            Cursor cursor2 = sqlDB.rawQuery("SELECT max(num) FROM notices;", null);
                            cursor2.moveToNext();
                            String a = cursor2.getString(0);
                            if (a.equals(N_NUM)) {
                                NoTice = true;
                            } else {
                                NoTice = false;
                            }
                        }
                        if (NoTice == true) {
                            Menu menu = navigationView.getMenu();
                            MenuItem it = menu.findItem(R.id.nav_notice);
                            it.setIcon(null);
                        } else {
                            Menu menu = navigationView.getMenu();
                            MenuItem it = menu.findItem(R.id.nav_notice);
                            it.setIcon(android.R.drawable.ic_popup_reminder);
                        }
                    }
                }
            } catch (JSONException e) {
                Log.d(TAG, "showResult_N : ", e);
            }
        }
    }

    class DeleteData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "id=" + id;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    class DeleteData2 extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(String... params) {
            String id = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "id=" + id;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();
                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);
                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
                bufferedReader.close();
                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData: Error ", e);
                return new String("Error: " + e.getMessage());
            }
        }
    }

    public String getID() {
        return ID;
    }

    public String getIP() {
        return IP;
    }

    public String getNAME() {
        return NAME;
    }

    public String getFIRST() {
        return FIRST;
    }
}