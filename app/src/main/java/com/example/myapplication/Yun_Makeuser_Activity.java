package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Yun_Makeuser_Activity extends AppCompatActivity {
    private static final String TAG2 = "phpquerytest", TAG_JSON = "webnautes", TAG_ID = "id", TAG_EMAIL = "email", TAG = "phptest";
    String mJsonString, IP, ID, EMAIL, temp = "", sex = "남자", email2, self = "1", pass_email = "no", pass_id = "no";
    static final int getimagesetting = 1001, REQUEST_CAMERA = 1;
    LinearLayout linear;
    private DatePicker mEditTextbirth;
    private RadioGroup group;
    private RadioButton man, girl;
    private EditText mEditTextpassword, mEditTextid, mEditTextName, mEditTextemail, mEditTextphone1, mEditTextphone2, mEditTextphone3, mEditTextself;
    private Button btn_email_check, btn_id_check;
    private TextView mTextViewResult;
    private Spinner s;
    private ImageView inputimg;

    void permission_init() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {    //권한 거절
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            } else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_CAMERA);
            }
        } else {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yun_makeuser_activity);
        mEditTextName = (EditText) findViewById(R.id.editText_main_name);
        mEditTextid = (EditText) findViewById(R.id.editText_main_id);
        mEditTextpassword = (EditText) findViewById(R.id.editText_main_password);
        group = (RadioGroup) findViewById(R.id.rgroup);
        man = (RadioButton) findViewById(R.id.rman);
        girl = (RadioButton) findViewById(R.id.rgirl);
        linear = (LinearLayout) findViewById(R.id.linear);
        mEditTextbirth = (DatePicker) findViewById(R.id.editText_main_birth);
        mEditTextemail = (EditText) findViewById(R.id.editText_main_email1);
        mEditTextphone1 = (EditText) findViewById(R.id.editText_main_phone1);
        mEditTextphone2 = (EditText) findViewById(R.id.editText_main_phone2);
        mEditTextphone3 = (EditText) findViewById(R.id.editText_main_phone3);
        mEditTextself = (EditText) findViewById(R.id.self);
        s = (Spinner) findViewById(R.id.editText_main_email2);
        mTextViewResult = (TextView) findViewById(R.id.textView_main_result);
        inputimg = (ImageView) findViewById(R.id.inputImage);
        mTextViewResult.setMovementMethod(new ScrollingMovementMethod());
        mEditTextbirth.setMaxDate(System.currentTimeMillis());
        permission_init();
        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        inputimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
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
                        sex = "남자";
                        break;
                    case R.id.rgirl:
                        sex = "여자";
                        break;
                }
            }
        });
        Button buttonInsert = (Button) findViewById(R.id.button_main_insert);
        btn_email_check = (Button) findViewById(R.id.email_check);
        btn_id_check = (Button) findViewById(R.id.id_check);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String name = mEditTextName.getText().toString();
                    String id = mEditTextid.getText().toString();
                    String password = mEditTextpassword.getText().toString();
                    Integer y = mEditTextbirth.getYear();
                    Integer m = mEditTextbirth.getMonth() + 1;
                    Integer d = mEditTextbirth.getDayOfMonth();
                    String birth = String.format("%04d-%02d-%02d", y, m, d);
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat sdf = new SimpleDateFormat("%04d-%02d-%02d");
                    String today = sdf.format(date);
                    String email = null;
                    if (self.equals("1")) {
                        email = mEditTextemail.getText().toString() + "@" + email2;
                    } else {
                        email = mEditTextemail.getText().toString() + "@" + mEditTextself.getText().toString();
                    }
                    String phone = mEditTextphone1.getText().toString() + "-" + mEditTextphone2.getText().toString() + "-" + mEditTextphone3.getText().toString();
                    if (id.isEmpty() || name.isEmpty() || password.isEmpty() || email.isEmpty() || mEditTextphone1.getText().toString().isEmpty() || mEditTextphone2.getText().toString().isEmpty() || mEditTextphone3.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                        builder.setTitle("값을 모두 입력해주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                        throw new Exception();
                    }
                    if (pass_email.equals("no") || pass_id.equals("no")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                        builder.setTitle("중복체크를 해주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                        throw new Exception();
                    }
                    NetworkInfo mNetworkState = getNetworkInfo();
                    if (mNetworkState != null && mNetworkState.isConnected()) {
                        InsertData task = new InsertData();
                        Integer first = 1;
                        task.execute("http://" + IP + "/insert.php", id, name, password, birth, sex, phone, email, "어플", today, first.toString(), temp);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                        builder.setTitle("가입이 완료되었습니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });
                        builder.show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                        builder.setCancelable(false);
                        builder.setTitle("인터넷 연결을 확인해 주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                    }
                } catch (Exception e) {
                }
            }
        });
        btn_email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mEditTextemail.getText().toString().isEmpty()) throw new Exception();
                    GetData task = new GetData();
                    if (self.equals("1")) {
                        task.execute(mEditTextemail.getText().toString() + "@" + email2);
                    } else {
                        task.execute(mEditTextemail.getText().toString() + "@" + mEditTextself.getText().toString());
                    }
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                    builder.setTitle("이메일이 잘못됐습니다.");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }
            }
        });
        btn_id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mEditTextid.getText().toString().isEmpty()) throw new Exception();
                    GetData_id task2 = new GetData_id();
                    task2.execute(mEditTextid.getText().toString());
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                    builder.setTitle("아이디가 잘못됐습니다.");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                }
            }
        });
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_Makeuser_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            mTextViewResult.setText(result);
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
            String img = (String) params[11];
            String serverURL = (String) params[0];
            String postParameters = "id=" + id + "&name=" + name + "&password=" + password + "&birth=" + birth + "&sex=" + sex + "&phone=" + phone + "&email=" + email + "&thema=" + themas + "&recently=" + recently + "&first=" + first.toString() + "&image=" + img;
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
            progressDialog = ProgressDialog.show(Yun_Makeuser_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG2, "response - " + result);
            if (result != null) {
                mJsonString = result;
                showResult();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword = params[0];
            String serverURL = "http://" + IP + "/query.php";
            String postParameters = "email=" + searchKeyword;
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
                Log.d(TAG2, "response code - " + responseStatusCode);
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
                Log.d(TAG2, "InsertData: Error ", e);
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
                EMAIL = item.getString(TAG_EMAIL);
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                builder.setTitle("이미 동일한 이메일이 있습니다.");
                builder.setPositiveButton("확인", null);
                builder.create().show();
            }
        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
            builder.setTitle("사용 가능한 이메일입니다.");
            builder.setPositiveButton("확인", null);
            builder.create().show();
            btn_email_check.setEnabled(false);
            pass_email = "yes";
            mEditTextemail.setEnabled(false);
            s.setEnabled(false);
            mEditTextself.setEnabled(false);
        }
    }

    class GetData_id extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Yun_Makeuser_Activity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG2, "response - " + result);
            if (result != null) {
                mJsonString = result;
                showResult_id();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword = params[0];
            String serverURL = "http://" + IP + "/id_check.php";
            String postParameters = "id=" + searchKeyword;
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
                Log.d(TAG2, "response code - " + responseStatusCode);
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
                Log.d(TAG2, "InsertData: Error ", e);
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult_id() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                ID = item.getString(TAG_ID);
                AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
                builder.setTitle("이미 동일한 아이디가 있습니다.");
                builder.setPositiveButton("확인", null);
                builder.create().show();
            }
        } catch (JSONException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Yun_Makeuser_Activity.this);
            builder.setTitle("사용 가능한 아이디입니다.");
            builder.setPositiveButton("확인", null);
            builder.create().show();
            btn_id_check.setEnabled(false);
            pass_id = "yes";
            mEditTextid.setEnabled(false);
        }
    }

    void addImage() {
        Intent intent = new Intent(getApplicationContext(), In_Inputimg_Activity.class);
        startActivityForResult(intent, getimagesetting);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getimagesetting) {
            if (resultCode == RESULT_OK) {
                Bitmap selPhoto = null;
                selPhoto = (Bitmap) data.getParcelableExtra("bitmap");
                inputimg.setImageBitmap(selPhoto);
                BitMapToString(selPhoto);
            }
        }
    }

    public void BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] arr = baos.toByteArray();
        String image = Base64.encodeToString(arr, Base64.DEFAULT);
        try {
            temp = URLEncoder.encode(image, "utf-8");
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    public static Bitmap StringToBitMap(String image) {
        Log.e("StringToBitMap", "StringToBitMap");
        try {
            byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("StringToBitMap", "good");
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
    }
}