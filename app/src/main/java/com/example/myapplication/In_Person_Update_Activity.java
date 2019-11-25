package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class In_Person_Update_Activity extends AppCompatActivity {
    private static final String TAG = "phpquerytest";
    private RadioGroup group;
    private RadioButton man, girl;
    Bitmap getBlob;
    static final int getimagesetting = 1001, REQUEST_CAMERA = 1;
    LinearLayout linear;
    private DatePicker mEditTextbirth;
    String NAME, BIRTH, SEX, PHONE, EMAIL, FIRST, PASSWORD, ID, IP, mJsonString, temp = "";
    TextView emails, first;
    EditText names, mEditTextphone1, mEditTextphone2, mEditTextphone3;
    Button up_b;
    ImageView inputImage;

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

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_person_update_activity);
        man = (RadioButton) findViewById(R.id.rman);
        linear = (LinearLayout) findViewById(R.id.linear);
        girl = (RadioButton) findViewById(R.id.rgirl);
        mEditTextbirth = (DatePicker) findViewById(R.id.up_birth);
        group = (RadioGroup) findViewById(R.id.rgroup);
        names = (EditText) findViewById(R.id.up_name);
        inputImage = (ImageView) findViewById(R.id.inputImage);
        mEditTextphone1 = (EditText) findViewById(R.id.editText_main_phone1);
        mEditTextphone2 = (EditText) findViewById(R.id.editText_main_phone2);
        mEditTextphone3 = (EditText) findViewById(R.id.editText_main_phone3);
        emails = (TextView) findViewById(R.id.email);
        first = (TextView) findViewById(R.id.first);
        up_b = (Button) findViewById(R.id.up_person);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        permission_init();
        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        PASSWORD = intent.getStringExtra("PASSWORD");
        NAME = intent.getStringExtra("NAME");
        names.setText(NAME);
        BIRTH = intent.getStringExtra("BIRTH");
        SEX = intent.getStringExtra("SEX");
        PHONE = intent.getStringExtra("PHONE");
        EMAIL = intent.getStringExtra("EMAIL");
        emails.setText(EMAIL);
        FIRST = intent.getStringExtra("FIRST");
        first.setText(FIRST);
        ID = intent.getStringExtra("ID");
        String[] birth = BIRTH.split("-");
        String[] phone = PHONE.split("-");
        mEditTextphone1.setText(phone[0]);
        mEditTextphone2.setText(phone[1]);
        mEditTextphone3.setText(phone[2]);
        mEditTextbirth.updateDate(Integer.parseInt(birth[0]), Integer.parseInt(birth[1]) - 1, Integer.parseInt(birth[2]));
        if (SEX.equals("남자")) {
            man.setChecked(true);
            girl.setChecked(false);
            SEX = "남자";
        } else {
            man.setChecked(false);
            girl.setChecked(true);
            SEX = "여자";
        }
        GetData1 tasks = new In_Person_Update_Activity.GetData1();
        tasks.execute(ID);
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
        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addImage();
            }
        });
        up_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (names.getText().toString().isEmpty() || mEditTextphone1.getText().toString().isEmpty() || mEditTextphone2.getText().toString().isEmpty() || mEditTextphone3.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(In_Person_Update_Activity.this);
                        builder.setTitle("값을 모두 입력해주세요");
                        builder.setPositiveButton("확인", null);
                        builder.create().show();
                        throw new Exception();
                    }
                    Integer y = mEditTextbirth.getYear();
                    Integer m = mEditTextbirth.getMonth() + 1;
                    Integer d = mEditTextbirth.getDayOfMonth();
                    String birth;
                    if (m < 10) {
                        birth = y.toString() + "-0" + m.toString() + "-" + d.toString();
                    } else {
                        birth = y.toString() + "-" + m.toString() + "-" + d.toString();
                    }
                    String phone = mEditTextphone1.getText().toString() + "-" + mEditTextphone2.getText().toString() + "-" + mEditTextphone3.getText().toString();
                    NetworkInfo mNetworkState = getNetworkInfo();
                    if (mNetworkState != null && mNetworkState.isConnected()) {
                        In_Person_Update_Activity.GetData task = new In_Person_Update_Activity.GetData();
                        task.execute(ID, names.getText().toString(), birth, SEX, phone, temp);
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(In_Person_Update_Activity.this);
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
                inputImage.setImageBitmap(selPhoto);
                BitMapToString(selPhoto);
            } else {
                inputImage.setImageResource(R.mipmap.camera);
                temp = "";
            }
        } else if (requestCode == 01) {
            inputImage.setImageResource(R.mipmap.camera);
            temp = "";
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

    private NetworkInfo getNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    class GetData extends AsyncTask<String, Void, String> {
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
            String name = params[1];
            String birth = params[2];
            String sex = params[3];
            String phone = params[4];
            String img = params[5];
            String serverURL = "http://" + IP + "/img_update_test.php";
            Log.i("img로그 :", img + "로그보자\n");
            String postParameters = "&id=" + id + "&name=" + name + "&birth=" + birth + "&sex=" + sex + "&phone=" + phone + "&image=" + img;
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
                byte[] bImage = Base64.decode(result, 0);
                String image = Base64.encodeToString(bImage, Base64.DEFAULT);
                try {
                    temp = URLEncoder.encode(image, "utf-8");
                } catch (Exception e) {
                    Log.e("exception", e.toString());
                }
                ByteArrayInputStream bais = new ByteArrayInputStream(bImage);
                getBlob = BitmapFactory.decodeStream(bais);
                if (getBlob != null) {
                    inputImage.setImageBitmap(getBlob);
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
}



