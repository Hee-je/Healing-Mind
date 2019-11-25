package com.example.myapplication;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Yun_Idcheck_Activity extends AppCompatActivity {
    private EditText mEditTextphone1, mEditTextphone2, mEditTextphone3, mEditTextname;
    String IP, ID, NAME, SEX = "남자", PHONE, EMAIL, TAG = "phptest", what, email2, self = "1";
    Button buttonInsert;
    LinearLayout email_insert, name_insert, linear;
    EditText mEditTextself, mEditTextemail;
    private DatePicker mEditTextbirth;
    private RadioGroup group;
    private Spinner s;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_idcheck_activity);
        Intent get_intent = getIntent();
        IP = get_intent.getStringExtra("IP");
        what = get_intent.getStringExtra("what");
        ID = get_intent.getStringExtra("ID");
        NAME = get_intent.getStringExtra("NAME");
        EMAIL = get_intent.getStringExtra("EMAIL");
        group = (RadioGroup) findViewById(R.id.rgroup);
        mEditTextphone1 = (EditText) findViewById(R.id.editText_main_phone1);
        email_insert = (LinearLayout) findViewById(R.id.email_insert);
        mEditTextphone2 = (EditText) findViewById(R.id.editText_main_phone2);
        mEditTextphone3 = (EditText) findViewById(R.id.editText_main_phone3);
        mEditTextbirth = (DatePicker) findViewById(R.id.editText_main_birth);
        mEditTextemail = (EditText) findViewById(R.id.editText_main_email1);
        mEditTextself = (EditText) findViewById(R.id.self);
        mEditTextname = (EditText) findViewById(R.id.editText_main_name);
        name_insert = (LinearLayout) findViewById(R.id.name_insert);
        linear = (LinearLayout) findViewById(R.id.linear);
        mEditTextname.setText(NAME);
        s = (Spinner) findViewById(R.id.editText_main_email2);
        buttonInsert = (Button) findViewById(R.id.button_main_insert);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        if (what.equals("페이스북")) {
            name_insert.setVisibility(View.GONE);
            email_insert.setVisibility(View.VISIBLE);
        } else if (what.equals("구글")) {
            name_insert.setVisibility(View.VISIBLE);
            email_insert.setVisibility(View.GONE);
        } else {
            name_insert.setVisibility(View.GONE);
            email_insert.setVisibility(View.GONE);
        }
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                email2 = parent.getItemAtPosition(position).toString();
                if (position == 3) {
                    mEditTextself.setVisibility(View.VISIBLE);
                    self = "2";
                } else {
                    mEditTextself.setVisibility(View.GONE);
                    self = "1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.rman:
                        SEX = "남자";
                        break;
                    case R.id.rgirl:
                        SEX = "여자";
                        break;
                }
            }
        });
        PHONE = mEditTextphone1.getText().toString() + "-" + mEditTextphone2.getText().toString() + "-" + mEditTextphone3.getText().toString();
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Integer y = mEditTextbirth.getYear();
                    Integer m = mEditTextbirth.getMonth() + 1;
                    Integer d = mEditTextbirth.getDayOfMonth();
                    String birth;
                    if (m < 10) {
                        birth = y.toString() + "-0" + m.toString() + "-" + d.toString();
                    } else {
                        birth = y.toString() + "-" + m.toString() + "-" + d.toString();
                    }
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String today = sdf.format(date);
                    PHONE = mEditTextphone1.getText().toString() + "-" + mEditTextphone2.getText().toString() + "-" + mEditTextphone3.getText().toString();
                    if (mEditTextphone1.getText().toString().isEmpty() || mEditTextphone2.getText().toString().isEmpty() || mEditTextphone3.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Idcheck_Activity.this);
                        builder.setTitle("값을 모두 입력해주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                        throw new Exception();
                    }
                    NetworkInfo mNetworkState = getNetworkInfo();
                    if (mNetworkState != null && mNetworkState.isConnected()) {
                        InsertData task = new InsertData();
                        Integer first = 1;
                        if (what.equals("페이스북")) {
                            if (self.equals("1")) {
                                EMAIL = mEditTextemail.getText().toString() + "@" + email2;
                            } else {
                                EMAIL = mEditTextemail.getText().toString() + "@" + mEditTextself.getText().toString();
                            }
                        }
                        task.execute("http://" + IP + "/insert.php", ID, NAME, ID, birth, SEX, PHONE, EMAIL, what, today, first.toString());
                        mEditTextphone1.setText("");
                        mEditTextphone2.setText("");
                        mEditTextphone3.setText("");
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Idcheck_Activity.this);
                        builder.setTitle("가입이 완료되었습니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent bye = new Intent(getApplicationContext(), In_Frame_Activity.class);
                                bye.putExtra("IP", IP);
                                bye.putExtra("ID", ID);
                                bye.putExtra("EMAIL", EMAIL);
                                bye.putExtra("PASSWORD", ID);
                                bye.putExtra("NAME", NAME);
                                startActivity(bye);
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Idcheck_Activity.this);
                        builder.setCancelable(false);
                        builder.setTitle("인터넷 연결을 확인해 주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_Idcheck_Activity.this,
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
            String id = (String) params[1];
            String name = (String) params[2];
            String password = (String) params[3];
            String birth = (String) params[4];
            String sex = (String) params[5];
            String phone = (String) params[6];
            String email = (String) params[7];
            String themas = (String) params[8];
            String recently = (String) params[9];
            Integer first = Integer.parseInt(params[10]);
            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&name=" + name + "&password=" + password + "&birth=" + birth + "&sex=" + sex + "&phone=" + phone + "&email=" + email + "&thema=" + themas + "&recently=" + recently + "&first=" + first.toString();
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
}

