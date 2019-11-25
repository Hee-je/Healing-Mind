package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class In_Notice_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String NAME, IP, ID, mJsonString;
    LinearLayout linear;
    ListView listView;
    TextView N;
    ArrayList<String> N_num = new ArrayList<>();
    ArrayList<String> N_title = new ArrayList<>();
    ArrayList<String> N_notice = new ArrayList<>();
    ArrayList<String> N_date = new ArrayList<>();
    ArrayList<HashMap<String, String>> mArrayList;
    Integer list_point = 5;
    boolean first = true;
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_NUM = "num", TAG_TITLE = "title", TAG_DATE = "date", TAG_NOTICE = "notice";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_notice_activitiy);
        myHelper = new MyDBHelper(this);
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        NetworkInfo mNetworkState = getNetworkInfo();
        if (mNetworkState != null && mNetworkState.isConnected()) {
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(In_Notice_Activity.this);
            builder.setCancelable(false);
            builder.setTitle("인터넷 연결을 확인해 주세요");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            builder.create().show();
        }
        N = findViewById(R.id.notice);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.listView);
        In_Notice_Activity.GetData task = new In_Notice_Activity.GetData();
        mArrayList = new ArrayList<>();
        task.execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                N.setText(N_notice.get(position));
            }
        });
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
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://" + IP + "/Notice.php";
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
    }

    private void showResult() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            boolean F = true;
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject item = jsonArray.getJSONObject(i);
                N_num.add(item.getString(TAG_NUM));
                N_title.add(item.getString(TAG_TITLE));
                N_notice.add(item.getString(TAG_NOTICE));
                N_date.add(item.getString(TAG_DATE));
                String dates = item.getString(TAG_DATE);
                String title = item.getString(TAG_TITLE);
                String notice = item.getString(TAG_NOTICE);
                String num = item.getString(TAG_NUM);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_NUM, num);
                hashMap.put(TAG_TITLE, title);
                hashMap.put(TAG_NOTICE, notice);
                hashMap.put(TAG_DATE, dates);
                mArrayList.add(hashMap);
                sqlDB = myHelper.getWritableDatabase();
                try {
                    Cursor cursor = sqlDB.rawQuery("Select *from notices where num=" + num + ";", null);
                    if (!cursor.moveToNext()) {
                        sqlDB.execSQL("INSERT INTO notices VALUES (" + num + ");");
                    }
                } catch (Exception e) {
                    sqlDB.execSQL("INSERT INTO notices VALUES (" + num + ");");
                }
                sqlDB.close();
                if (F) {
                    F = false;
                    N.setText(notice);
                }
            }
            ListAdapter adapter = new SimpleAdapter(
                    In_Notice_Activity.this, mArrayList, R.layout.in_notice_list_activity,
                    new String[]{TAG_TITLE, TAG_DATE},
                    new int[]{R.id.notice_title, R.id.notice_date}
            );
            listView.setAdapter(adapter);
            listView.setSelection(0);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    public void onResume() {
        super.onResume();
        first = true;
        list_point = 0;
        N_num = new ArrayList<>();
        N_title = new ArrayList<>();
        N_notice = new ArrayList<>();
        N_date = new ArrayList<>();
    }
}
