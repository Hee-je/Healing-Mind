package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Je_PWsearch_Fragment extends Fragment {
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_ID = "id", TAG_NAME = "name", TAG_EMAIL = "email", TAG_PHONE = "phone";
    EditText id, ps_email, Anumber, ps_phone;
    String[] arr;
    String ID, NAME, PHONE, EMAIL, IP, mJsonString, one, two, three, four, five, six, seven, body;
    Button ps_search, plus, next;
    Random random = new Random();
    LinearLayout linear;
    String check_mail = null;
    CountDownTimer countDownTimer;
    RadioGroup radiogroup;
    RadioButton radio_email, radio_phone;
    TextView CountText;
    Boolean choice = true;


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            verificationCallbacks;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private String phoneVerificationId;

    int MILLISINFUTURE = 120 * 1000;
    final int COUNT_DOWN_INTERVAL = 1000;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Je_PWsearch_Fragment() {
    }

    public static Je_PWsearch_Fragment newInstance(String param1, String param2) {
        Je_PWsearch_Fragment fragment = new Je_PWsearch_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this.getActivity(), new String[]{Manifest.permission.SEND_SMS}, 1);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.je_pwsearch_fragment, container, false);

        ps_search = (Button) view.findViewById(R.id.ps_search);
        plus = (Button) view.findViewById(R.id.plus);
        next = (Button) view.findViewById(R.id.next);
        id = (EditText) view.findViewById(R.id.id);
        radiogroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        ps_email = (EditText) view.findViewById(R.id.ps_email);
        ps_phone = (EditText) view.findViewById(R.id.ps_phone);
        Anumber = (EditText) view.findViewById(R.id.Anumber);
        CountText = (TextView) view.findViewById(R.id.CountText);
        radio_phone = (RadioButton) view.findViewById(R.id.radio_phone);
        radio_email = (RadioButton) view.findViewById(R.id.radio_email);
        arr = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_email:
                        ps_phone.setVisibility(View.GONE);
                        ps_email.setVisibility(View.VISIBLE);
                        choice = true;
                        ps_phone.setText("");
                        break;
                    case R.id.radio_phone:
                        ps_email.setVisibility(View.GONE);
                        ps_phone.setVisibility(View.VISIBLE);
                        choice = false;
                        ps_email.setText("");
                        break;
                }
            }
        });

        ps_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    radio_email.setEnabled(false);
                    radio_phone.setEnabled(false);

                    if (choice) {
                        if (id.getText().toString().isEmpty() || ps_email.getText().toString().isEmpty())
                            throw new Exception();
                        GetData_pw task = new GetData_pw();
                        task.execute(id.getText().toString(), ps_email.getText().toString());
                    } else {
                        if (id.getText().toString().isEmpty() || ps_phone.getText().toString().isEmpty())
                            throw new Exception();
                        GetData_pw2 task2 = new GetData_pw2();
                        task2.execute(id.getText().toString(), ps_phone.getText().toString());
                    }
                } catch (Exception e) {
                    radio_email.setEnabled(true);
                    radio_phone.setEnabled(true);
                    AlertDialog.Builder builder = new AlertDialog.Builder((Je_All_Search_Activity) getActivity());
                    builder.setTitle("정보를 확인해주세요");
                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                    Log.e("에러",e.toString());
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (choice) {
                        if (body.equals(Anumber.getText().toString())) {
                            Intent intent = new Intent(getActivity(), Je_Pssearch_Activity.class);
                            intent.putExtra("id", ID);
                            intent.putExtra("IP", IP);
                            startActivity(intent);
                            getActivity().finish();
                        } else {
                            throw new Exception();
                        }
                    }

                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder((Je_All_Search_Activity) getActivity());
                    builder.setTitle("인증번호가 잘못됐습니다.");

                    builder.setPositiveButton("확인", null);
                    builder.create().show();
                    Log.d(TAG, "showResult : ", e);
                }
                try {
                    if (!choice) {
                        verifyCode();
                    }
                } catch (Exception e) {

                }
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData_email task_email = new GetData_email();
                task_email.execute(check_mail, body);
                Anumber.setEnabled(true);
                countDownTimer();
                countDownTimer.start();
                plus.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof Je_All_Search_Activity) {
            IP = ((Je_All_Search_Activity) getActivity()).getData();
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    class GetData_pw extends AsyncTask<String, Void, String> {
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
                showResult_pw();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://" + IP + "/query2.php";
            String postParameters = "id=" + searchKeyword1 + "&email=" + searchKeyword2;
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

    private void showResult_pw() {
        try {
            AlertDialog.Builder alert_email = new AlertDialog.Builder((Je_All_Search_Activity) getActivity());
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                ID = item.getString(TAG_ID);
                EMAIL = item.getString(TAG_EMAIL);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, ID);
                hashMap.put(TAG_EMAIL, EMAIL);
                one = arr[random.nextInt(36)];
                two = arr[random.nextInt(36)];
                three = arr[random.nextInt(36)];
                four = arr[random.nextInt(36)];
                five = arr[random.nextInt(36)];
                six = arr[random.nextInt(36)];
                seven = arr[random.nextInt(36)];
                body = one + two + three + four + five + six + seven;
                check_mail = ps_email.getText().toString();
                GetData_email task_email = new GetData_email();
                task_email.execute(ps_email.getText().toString(), body);
                id.setVisibility(View.GONE);
                ps_email.setVisibility(View.GONE);
                ps_search.setVisibility(View.GONE);
                Anumber.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                countDownTimer();
                countDownTimer.start();
            }
            id.setText("");
            ps_email.setText("");
        } catch (JSONException e) {
            radio_email.setEnabled(true);
            radio_phone.setEnabled(true);
            AlertDialog.Builder builder = new AlertDialog.Builder((Je_All_Search_Activity) getActivity());
            builder.setTitle("이메일이 잘못됐습니다.");
            builder.setPositiveButton("확인", null);
            builder.create().show();
            Log.d(TAG, "showResult : ", e);
        }
    }

    class GetData_pw2 extends AsyncTask<String, Void, String> {
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
                showResult_pw2();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://" + IP + "/query3.php";
            String postParameters = "id=" + searchKeyword1 + "&phone=" + searchKeyword2;
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

    private void showResult_pw2() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                ID = item.getString(TAG_ID);
                PHONE = item.getString(TAG_PHONE);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_ID, ID);
                hashMap.put(TAG_PHONE, PHONE);
                one = arr[random.nextInt(36)];
                two = arr[random.nextInt(36)];
                three = arr[random.nextInt(36)];
                four = arr[random.nextInt(36)];
                five = arr[random.nextInt(36)];
                six = arr[random.nextInt(36)];
                seven = arr[random.nextInt(36)];
                try {
                    phone_serch();
                    id.setVisibility(View.GONE);
                    ps_phone.setVisibility(View.GONE);
                    ps_search.setVisibility(View.GONE);
                    Anumber.setVisibility(View.VISIBLE);
                    next.setVisibility(View.VISIBLE);
                    countDownTimer();
                    countDownTimer.start();
                } catch (Exception e) {
                }
            }
            id.setText("");
            ps_phone.setText("");
        } catch (JSONException e) {
            radio_email.setEnabled(true);
            radio_phone.setEnabled(true);
            AlertDialog.Builder builder = new AlertDialog.Builder((Je_All_Search_Activity) getActivity());
            builder.setTitle("번호가 잘못됐습니다.");
            builder.setPositiveButton("확인", null);
            builder.create().show();
            Log.d(TAG, "showResult : ", e);
        }
    }

    class GetData_email extends AsyncTask<String, Void, String> {
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
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String searchKeyword1 = params[0];
            String searchKeyword2 = params[1];
            String serverURL = "http://" + IP + "/mail.php";
            String postParameters = "to=" + searchKeyword1 + "&body=" + searchKeyword2;
            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(1000);
                httpURLConnection.setConnectTimeout(1000);
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

    public void countDownTimer() {
        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
            public void onTick(long millisUntilFinished) {
                long emailAuthCount = millisUntilFinished / 1000;
                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                    CountText.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                    CountText.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                }
                if (emailAuthCount == 170) {
                }
            }

            public void onFinish() {
                countDownTimer.cancel();
                Anumber.setEnabled(false);
                next.setVisibility(View.GONE);
                plus.setVisibility(View.VISIBLE);
                CountText.setText(String.valueOf("Finish ."));
            }
        };
    }


    void phone_serch() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.setLanguageCode("kr");
        mAuth.useAppLanguage();
        sendCode();
    }

    public void sendCode() {
        String sub = ps_phone.getText().toString();
        sub.replace("010", "10");
        String phoneNumber = "+82" + sub;

        setUpVerificationCallbacks();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                    // Phone number to verify
                120,                          // Timeout duration
                TimeUnit.SECONDS,               // Unit of timeout
                getActivity(),                   // Activity (for callback binding)
                verificationCallbacks);
    }


    private void setUpVerificationCallbacks() {

        verificationCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(
                            @NonNull PhoneAuthCredential credential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            // Invalid Request
                            Log.e("파이어베이스 에러", "Invalid credential: "
                                    + e.getLocalizedMessage());
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            // SMS quota exceeded
                            Log.e("파이어베이스 초과", "SMS Quota exceeded.");
                        }
                    }

                    @Override
                    public void onCodeSent(String verificationId,
                                           PhoneAuthProvider.ForceResendingToken token) {

                        phoneVerificationId = verificationId;
                        resendToken = token;


                    }
                };
    }

    public void verifyCode() {

        String code = Anumber.getText().toString();

        PhoneAuthCredential credential =
                PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), (task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(getActivity(), Je_Pssearch_Activity.class);
                        intent.putExtra("id", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                        getActivity().finish();

                        FirebaseUser user = task.getResult().getUser();
                        Log.i("로그인 완료", user.getPhoneNumber());
                    } else {
                        if (task.getException() instanceof
                                FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            AlertDialog.Builder builder = new AlertDialog.Builder((Je_All_Search_Activity) getActivity());
                            builder.setTitle("인증번호가 잘못됐습니다.");

                            builder.setPositiveButton("확인", null);
                            builder.create().show();
                        }
                    }
                }));
    }




}