package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

public class Yun_ShowBoard_Activity extends AppCompatActivity {
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_NAME = "name", TAG_MEMO = "memo", TAG_DATE = "date", TAG_REFERENCE = "reference", TAG_NUM = "num", TAG_ID = "id";
    TextView memo, name, title, date;
    Button up, del, com;
    Integer y, mon, day;
    EditText edit_comment;
    String check, ID, IP, NAME, NUM, DATE, MEMO, mJsonString, dates, sub_in;
    LinearLayout listlayout, linear;
    ArrayList<String> comment_memo = new ArrayList<>();
    ArrayList<String> comment_id = new ArrayList<>();
    ArrayList<String> comment_date = new ArrayList<>();
    ArrayList<String> comment_name = new ArrayList<>();
    ArrayList<String> comment_num = new ArrayList<>();
    ArrayList<String> comment_reference = new ArrayList<>();
    ArrayList<HashMap<String, String>> mArrayList;
    ArrayAdapter<String> adapter;
    ListView listViews;
    ScrollView scrolView;
    DatePicker dday;
    private ProgressBar progressBar;
    GetData task = new GetData();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_showboard_activity);
        memo = (TextView) findViewById(R.id.txtmemo);
        memo.setMovementMethod(new ScrollingMovementMethod());
        name = (TextView) findViewById(R.id.txtname);
        listlayout = (LinearLayout) findViewById(R.id.listlayout);
        linear = (LinearLayout) findViewById(R.id.linear);
        scrolView = (ScrollView) findViewById(R.id.scrollView);
        edit_comment = (EditText) findViewById(R.id.comeent_edit);
        title = (TextView) findViewById(R.id.txttitle);
        date = (TextView) findViewById(R.id.txtdate);
        up = (Button) findViewById(R.id.board_update);
        com = (Button) findViewById(R.id.comment_btn);
        del = (Button) findViewById(R.id.board_delete);
        listViews = (ListView) findViewById(R.id.listViews);
        dday = (DatePicker) findViewById(R.id.dpick);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listViews.setAdapter(adapter);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        final SwipeRefreshLayout swipeRefreshLayout2 = findViewById(R.id.swipe_refresh_layout2);
        Intent intent = getIntent();
        name.setText("작성자 : " + intent.getStringExtra("board_NAME"));
        memo.setText(intent.getStringExtra("MEMO"));
        MEMO = intent.getStringExtra("MEMO");
        date.setText("작성일 : " + intent.getStringExtra("DATE"));
        DATE = intent.getStringExtra("DATE");
        title.setText("제목 : " + intent.getStringExtra("TITLE"));
        check = intent.getStringExtra("CHECK");
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        NUM = intent.getStringExtra("NUM");
        NAME = intent.getStringExtra("NAME");
        sub_in = intent.getStringExtra("TITLE");
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        if (check.equals("yes")) {
            up.setVisibility(View.VISIBLE);
            del.setVisibility(View.VISIBLE);
        }
        mArrayList = new ArrayList<>();
        task.execute("http://" + IP + "/comment.php", NUM);
        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent up = new Intent(getApplicationContext(), In_Board_Update.class);
                up.putExtra("NUM", NUM);
                up.putExtra("TITLE", sub_in);
                up.putExtra("MEMO", memo.getText().toString());
                up.putExtra("NAME", NAME);
                up.putExtra("DATE", DATE);
                up.putExtra("ID", ID);
                up.putExtra("IP", IP);
                up.putExtra("CHECK", check);
                startActivity(up);
                finish();
            }
        });
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_ShowBoard_Activity.this);
                builder.setMessage("게시물을 삭제하시겠습니까?");
                builder.setNegativeButton("아니오", null);
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteData task2 = new DeleteData();
                        task2.execute("http://" + IP + "/delete_board.php", NUM);
                        DeleteData2 task3 = new DeleteData2();
                        task3.execute("http://" + IP + "/delete_comment.php", NUM);
                        Toast.makeText(getApplicationContext(), "게시물이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
                builder.create().show();
            }
        });
        com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (edit_comment.getText().toString().isEmpty()) {
                        throw new Exception();
                    }
                    y = dday.getYear();
                    mon = dday.getMonth() + 1;
                    day = dday.getDayOfMonth();
                    dates = y.toString() + "-" + mon.toString() + "-" + day.toString();
                    InsertData task6 = new InsertData();
                    task6.execute("http://" + IP + "/comment_upload.php", ID, NAME, NUM, edit_comment.getText().toString(), dates);
                    Toast.makeText(getApplicationContext(), "작성이 완료되셨습니다", Toast.LENGTH_LONG).show();
                    edit_comment.setText("");
                    onRestart();
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Yun_ShowBoard_Activity.this);
                    builder.setTitle("올바른 문자를 입력해 주세요");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }
            }
        });

        swipeRefreshLayout2.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onRestart();
                swipeRefreshLayout2.setRefreshing(false);
            }
        });

        listViews.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrolView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        listViews.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    if (ID.equals(comment_id.get(position))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_ShowBoard_Activity.this);
                        builder.setMessage("댓글을 삭제하시겠습니까?");
                        builder.setNegativeButton("아니오", null);
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DeleteData3 task4 = new DeleteData3();
                                task4.execute("http://" + IP + "/select_comment.php", comment_num.get(position));
                                Toast.makeText(getApplicationContext(), "댓글이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                edit_comment.setText("");
                                onRestart();
                                listViews.setAdapter(adapter);
                            }
                        });
                        builder.create().show();
                    }
                } catch (Exception e) {
                }
                return false;
            }
        });
    }

    class DeleteData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_ShowBoard_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String num = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "num=" + num;
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
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_ShowBoard_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String reference = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "reference=" + reference;
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

    class DeleteData3 extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_ShowBoard_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String num_com = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "num=" + num_com;
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

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_ShowBoard_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }

        @Override
        protected String doInBackground(String... params) {
            String idz = (String) params[1];
            String namez = (String) params[2];
            String referencez = (String) params[3];
            String memoz = (String) params[4];
            String datez = (String) params[5];
            String serverURL = (String) params[0];
            String postParameters = "id=" + idz + "&name=" + namez + "&reference=" + referencez + "&memo=" + memoz + "&date=" + datez;
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

    class GetData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_ShowBoard_Activity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);
            if (result != null) {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String reference = (String) params[1];
            String serverURL = (String) params[0];
            String postParameters = "reference=" + reference;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));  ///////
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
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                JSONObject item = jsonArray.getJSONObject(i);
                comment_name.add(item.getString(TAG_NAME));
                comment_num.add(item.getString(TAG_NUM));
                comment_id.add(item.getString(TAG_ID));
                comment_date.add(item.getString(TAG_DATE));
                comment_reference.add(item.getString(TAG_REFERENCE));
                comment_memo.add(item.getString(TAG_MEMO));
                String names = "작성자 : " + item.getString(TAG_NAME);
                String memos = item.getString(TAG_MEMO);
                String dates = "작성일 : " + item.getString(TAG_DATE);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_NAME, names);
                hashMap.put(TAG_MEMO, memos);
                hashMap.put(TAG_DATE, dates);
                mArrayList.add(hashMap);
            }
            if (jsonArray.length() > 0) {
                listlayout.setVisibility(View.VISIBLE);
            } else {
                listlayout.setVisibility(View.GONE);
            }
            ListAdapter adapter = new SimpleAdapter(
                    Yun_ShowBoard_Activity.this, mArrayList, R.layout.yun_board_list_activity,
                    new String[]{TAG_NAME, TAG_DATE, TAG_MEMO},
                    new int[]{R.id.borad_list_name, R.id.borad_list_date, R.id.borad_list_title}
            );
            listViews.setAdapter(adapter);
            listViews.setSelection(0);
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        comment_memo = new ArrayList<>();
        comment_reference = new ArrayList<>();
        comment_date = new ArrayList<>();
        comment_name = new ArrayList<>();
        comment_id = new ArrayList<>();
        comment_num = new ArrayList<>();
        mArrayList = new ArrayList<>();
        task = new GetData();
        task.execute("http://" + IP + "/comment.php", NUM);
    }
}