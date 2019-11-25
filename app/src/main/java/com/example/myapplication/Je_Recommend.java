package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Je_Recommend extends Fragment {
    String ID, IP, mJsonString;
    LinearLayout linear;
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_NUM = "num", TAG_CONTENT = "content", TAG_TIME = "time";
    String[] my_num = new String[5];
    int[] my_num_int = new int[5];
    Button first, second, third, four;
    String[] num = new String[5];
    int[] num_int = new int[5];
    String[] content = new String[5];
    TextView my_max, my_min, max, time_max;
    int[] times_arr = new int[8];
    int[][] content_T = new int[8][5];
    Drawable fiveM, vocal, visual, con3, con4;
    TextView nodata;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Je_Recommend() {
    }

    public static Je_Recommend newInstance(String param1, String param2) {
        Je_Recommend fragment = new Je_Recommend();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.je_recommend, container, false);
        content[0] = "5분";
        content[1] = "그리기";
        content[2] = "발성";
        content[3] = "소리";
        content[4] = "시지각";
        fiveM = getContext().getResources().getDrawable(R.mipmap.one_w);
        vocal = getContext().getResources().getDrawable(R.mipmap.two_w);
        visual = getContext().getResources().getDrawable(R.mipmap.three_w);
        con3 = getContext().getResources().getDrawable(R.mipmap.five_w);
        con4 = getContext().getResources().getDrawable(R.mipmap.four_w);
        nodata = view.findViewById(R.id.Nodata);
        my_max = view.findViewById(R.id.my_max);
        my_min = view.findViewById(R.id.my_min);
        max = view.findViewById(R.id.max);
        time_max = view.findViewById(R.id.time_max);
        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.second);
        third = view.findViewById(R.id.third);
        four = view.findViewById(R.id.four);
        GetData task = new GetData();
        GetData2 task2 = new GetData2();
        GetData3 task3 = new GetData3();
        task.execute(ID);
        task2.execute();
        task3.execute(ID);
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
        if (getActivity() != null && getActivity() instanceof Je_Chart_Activity) {
            ID = ((Je_Chart_Activity) getActivity()).getData();
            IP = ((Je_Chart_Activity) getActivity()).getData2();
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
        void onFragmentInteraction(Uri uri);
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
            String id = params[0];
            String serverURL = "http://" + IP + "/chart_month_user.php";
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
                switch (item.getString(TAG_CONTENT)) {
                    case "5분":
                        my_num[0] = item.getString(TAG_NUM);
                        my_num_int[0] = Integer.parseInt(my_num[0]);
                        break;
                    case "그리기":
                        my_num[1] = item.getString(TAG_NUM);
                        my_num_int[1] = Integer.parseInt(my_num[1]);
                        break;
                    case "발성":
                        my_num[2] = item.getString(TAG_NUM);
                        my_num_int[2] = Integer.parseInt(my_num[2]);
                        break;
                    case "소리":
                        my_num[3] = item.getString(TAG_NUM);
                        my_num_int[3] = Integer.parseInt(my_num[3]);
                        break;
                    case "시지각":
                        my_num[4] = item.getString(TAG_NUM);
                        my_num_int[4] = Integer.parseInt(my_num[4]);
                        break;
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    class GetData2 extends AsyncTask<String, Void, String> {
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
                showResult2();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = "http://" + IP + "/chart_month.php";

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();
                OutputStream outputStream = httpURLConnection.getOutputStream();
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
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult2() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                switch (item.getString(TAG_CONTENT)) {
                    case "5분":
                        num[0] = item.getString(TAG_NUM);
                        num_int[0] = Integer.parseInt(num[0]);
                        break;
                    case "그리기":
                        num[1] = item.getString(TAG_NUM);
                        num_int[1] = Integer.parseInt(num[1]);
                        break;
                    case "발성":
                        num[2] = item.getString(TAG_NUM);
                        num_int[2] = Integer.parseInt(num[2]);
                        break;
                    case "소리":
                        num[3] = item.getString(TAG_NUM);
                        num_int[3] = Integer.parseInt(num[3]);
                        break;
                    case "시지각":
                        num[4] = item.getString(TAG_NUM);
                        num_int[4] = Integer.parseInt(num[4]);
                        break;
                }
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    class GetData3 extends AsyncTask<String, Void, String> {
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
                showResult3();
            }
        }

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];
            String serverURL = "http://" + IP + "/chart_time_user.php";
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
                errorString = e.toString();
                return null;
            }
        }
    }

    private void showResult3() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            String time_str;
            Integer time_int;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                time_str = item.getString(TAG_TIME);
                time_str = time_str.substring(0, 2);
                time_int = Integer.parseInt(time_str);
                if (time_int >= 21)  // 21시 ~ 23시 59분
                {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[7][0]++;
                            break;
                        case "그리기":
                            content_T[7][1]++;
                            break;
                        case "발성":
                            content_T[7][2]++;
                            break;
                        case "소리":
                            content_T[7][3]++;
                            break;
                        case "시지각":
                            content_T[7][4]++;
                            break;
                    }
                    times_arr[7]++;
                } else if (time_int >= 18) // 18시 ~ 20시 59분
                {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[6][0]++;
                            break;
                        case "그리기":
                            content_T[6][1]++;
                            break;
                        case "발성":
                            content_T[6][2]++;
                            break;
                        case "소리":
                            content_T[6][3]++;
                            break;
                        case "시지각":
                            content_T[6][4]++;
                            break;
                    }
                    times_arr[6]++;
                } else if (time_int >= 15) {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[5][0]++;
                            break;
                        case "그리기":
                            content_T[5][1]++;
                            break;
                        case "발성":
                            content_T[5][2]++;
                            break;
                        case "소리":
                            content_T[5][3]++;
                            break;
                        case "시지각":
                            content_T[5][4]++;
                            break;
                    }
                    times_arr[5]++;
                } else if (time_int >= 12) {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[4][0]++;
                            break;
                        case "그리기":
                            content_T[4][1]++;
                            break;
                        case "발성":
                            content_T[4][2]++;
                            break;
                        case "소리":
                            content_T[4][3]++;
                            break;
                        case "시지각":
                            content_T[4][4]++;
                            break;
                    }
                    times_arr[4]++;
                } else if (time_int >= 9) {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[3][0]++;
                            break;
                        case "그리기":
                            content_T[3][1]++;
                            break;
                        case "발성":
                            content_T[3][2]++;
                            break;
                        case "소리":
                            content_T[3][3]++;
                            break;
                        case "시지각":
                            content_T[3][4]++;
                            break;
                    }
                    times_arr[3]++;
                } else if (time_int >= 6) {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[2][0]++;
                            break;
                        case "그리기":
                            content_T[2][1]++;
                            break;
                        case "발성":
                            content_T[2][2]++;
                            break;
                        case "소리":
                            content_T[2][3]++;
                            break;
                        case "시지각":
                            content_T[2][4]++;
                            break;
                    }
                    times_arr[2]++;
                } else if (time_int >= 3) {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[1][0]++;
                            break;
                        case "그리기":
                            content_T[1][1]++;
                            break;
                        case "발성":
                            content_T[1][2]++;
                            break;
                        case "소리":
                            content_T[1][3]++;
                            break;
                        case "시지각":
                            content_T[1][4]++;
                            break;
                    }
                    times_arr[1]++;
                } else {
                    switch (item.getString(TAG_CONTENT)) {
                        case "5분":
                            content_T[0][0]++;
                            break;
                        case "그리기":
                            content_T[0][1]++;
                            break;
                        case "발성":
                            content_T[0][2]++;
                            break;
                        case "소리":
                            content_T[0][3]++;
                            break;
                        case "시지각":
                            content_T[0][4]++;
                            break;
                    }
                    times_arr[0]++;
                }
            }
            SetT();
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    void SetT() {
        int my_best = 0, my_bestN = 0;
        int my_m = 99999, my_mN = 99999;
        for (int i = 0; i < 5; i++) {
            if (my_bestN < my_num_int[i]) {
                my_bestN = my_num_int[i];
                my_best = i;
            }
            if (my_mN > my_num_int[i]) {
                my_mN = my_num_int[i];
                my_m = i;
            }
        }
        if (my_best == 0) {
            nodata.setVisibility(View.VISIBLE);
            first.setVisibility(View.GONE);
            second.setVisibility(View.GONE);
            third.setVisibility(View.GONE);
            four.setVisibility(View.GONE);
        } else {
            my_max.setText("가장 많이 본 명상은\n" + content[my_best] + " 명상 입니다");
            String strong = my_max.getText().toString();
            SpannableString spannableString = new SpannableString(strong);

            String word = "가장 많이 본 명상";
            int start = strong.indexOf(word);
            int end = start + word.length();

            String word_f = content[my_best] + " 명상";
            int start_f = strong.indexOf(word_f);
            int end_f = start_f + word_f.length();

            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB8B8B8")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new RelativeSizeSpan(1.4f), start, end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            switch (content[my_best]) {
                case "5분":
                    first.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_1));
                    first.setCompoundDrawablesWithIntrinsicBounds(fiveM, null, null, null);
                    //first.setText("5분 명상");
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#802F6699")), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(1.4f), start_f, end_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_max.setText(spannableString);
                    break;
                case "발성":
                    first.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_2));
                    first.setCompoundDrawablesWithIntrinsicBounds(vocal, null, null, null);
                    //first.setText("발성 명상");
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#80992F2F")), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(1.4f), start_f, end_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_max.setText(spannableString);
                    break;
                case "시지각":
                    first.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_3));
                    first.setCompoundDrawablesWithIntrinsicBounds(visual, null, null, null);
                    //first.setText("시지각 명상");
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#80BB6008")), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(1.4f), start_f, end_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_max.setText(spannableString);
                    break;
                case "그리기":
                    first.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_5));
                    first.setCompoundDrawablesWithIntrinsicBounds(con3, null, null, null);
                    //first.setText("그리기 명상");
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#806A3AB2")), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(1.4f), start_f, end_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_max.setText(spannableString);
                    break;
                case "소리":
                    first.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_4));
                    first.setCompoundDrawablesWithIntrinsicBounds(con4, null, null, null);
                    //first.setText("소리 명상");
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#8053933F")), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new RelativeSizeSpan(1.4f), start_f, end_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new StyleSpan(Typeface.BOLD), start_f, end_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_max.setText(spannableString);
                    break;
            }
            int finalMy_best = my_best;
            first.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content[finalMy_best].equals("5분")) {
                        Intent intent = new Intent(getActivity(), Yun_5minutes_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_best].equals("발성")) {
                        Intent intent = new Intent(getActivity(), Je_Vocalizationmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_best].equals("시지각")) {
                        Intent intent = new Intent(getActivity(), In_Visualperceptionmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_best].equals("그리기")) {
                        Intent intent = new Intent(getActivity(), In_Contents3Main_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_best].equals("소리")) {
                        Intent intent = new Intent(getActivity(), Yun_Category_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    }
                }
            });
            my_min.setText("가장 적게 본 명상은\n" + content[my_m] + " 명상 입니다");
            String strong2 = my_min.getText().toString();
            SpannableString spannableString2 = new SpannableString(strong2);

            String word2 = "가장 적게 본 명상";
            int start2 = strong2.indexOf(word2);
            int end2 = start2 + word2.length();

            String word2_f = content[my_m] + " 명상";
            int start2_f = strong2.indexOf(word2_f);
            int end2_f = start2_f + word2_f.length();

            spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB8B8B8")), start2, end2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString2.setSpan(new RelativeSizeSpan(1.4f), start2, end2, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            switch (content[my_m]) {
                case "5분":
                    second.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_1));
                    second.setCompoundDrawablesWithIntrinsicBounds(fiveM, null, null, null);
                    //second.setText("5분 명상");
                    spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#802F6699")), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new RelativeSizeSpan(1.4f), start2_f, end2_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_min.setText(spannableString2);
                    break;
                case "발성":
                    second.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_2));
                    second.setCompoundDrawablesWithIntrinsicBounds(vocal, null, null, null);
                    //second.setText("발성 명상");
                    spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#80992F2F")), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new RelativeSizeSpan(1.4f), start2_f, end2_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_min.setText(spannableString2);
                    break;
                case "시지각":
                    second.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_3));
                    second.setCompoundDrawablesWithIntrinsicBounds(visual, null, null, null);
                    //second.setText("시지각 명상");
                    spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#80BB6008")), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new RelativeSizeSpan(1.4f), start2_f, end2_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_min.setText(spannableString2);
                    break;
                case "그리기":
                    second.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_5));
                    second.setCompoundDrawablesWithIntrinsicBounds(con3, null, null, null);
                    //second.setText("그리기 명상");
                    spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#806A3AB2")), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new RelativeSizeSpan(1.4f), start2_f, end2_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_min.setText(spannableString2);
                    break;
                case "소리":
                    second.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_4));
                    second.setCompoundDrawablesWithIntrinsicBounds(con4, null, null, null);
                    //second.setText("소리 명상");
                    spannableString2.setSpan(new ForegroundColorSpan(Color.parseColor("#8053933F")), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new RelativeSizeSpan(1.4f), start2_f, end2_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString2.setSpan(new StyleSpan(Typeface.BOLD), start2_f, end2_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    my_min.setText(spannableString2);
                    break;
            }
            int finalMy_m = my_m;
            second.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content[finalMy_m].equals("5분")) {
                        Intent intent = new Intent(getActivity(), Yun_5minutes_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_m].equals("발성")) {
                        Intent intent = new Intent(getActivity(), Je_Vocalizationmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_m].equals("시지각")) {
                        Intent intent = new Intent(getActivity(), In_Visualperceptionmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_m].equals("그리기")) {
                        Intent intent = new Intent(getActivity(), In_Contents3Main_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalMy_m].equals("소리")) {
                        Intent intent = new Intent(getActivity(), Yun_Category_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    }
                }
            });
            int best = 0, bestN = 0;
            for (int i = 0; i < 5; i++) {
                if (bestN < num_int[i]) {
                    bestN = num_int[i];
                    best = i;
                }
            }
            max.setText("가장 인기 있는 명상은\n" + content[best] +  " 명상 입니다");
            String strong3 = max.getText().toString();
            SpannableString spannableString3 = new SpannableString(strong3);

            String word3 = "가장 인기 있는 명상";
            int start3 = strong3.indexOf(word3);
            int end3 = start3 + word3.length();

            String word3_f = content[best] +  " 명상";
            int start3_f = strong3.indexOf(word3_f);
            int end3_f = start3_f + word3_f.length();

            spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB8B8B8")), start3, end3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString3.setSpan(new RelativeSizeSpan(1.4f), start3, end3, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            switch (content[best]) {
                case "5분":
                    third.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_1));
                    third.setCompoundDrawablesWithIntrinsicBounds(fiveM, null, null, null);
                    //third.setText("5분 명상");
                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#802F6699")), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new RelativeSizeSpan(1.4f), start3_f, end3_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    max.setText(spannableString3);
                    break;
                case "발성":
                    third.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_2));
                    third.setCompoundDrawablesWithIntrinsicBounds(vocal, null, null, null);
                    //third.setText("발성 명상");
                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#80992F2F")), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new RelativeSizeSpan(1.4f), start3_f, end3_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    max.setText(spannableString3);
                    break;
                case "시지각":
                    third.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_3));
                    third.setCompoundDrawablesWithIntrinsicBounds(visual, null, null, null);
                    //third.setText("시지각 명상");
                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#80BB6008")), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new RelativeSizeSpan(1.4f), start3_f, end3_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    max.setText(spannableString3);
                    break;
                case "그리기":
                    third.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_5));
                    third.setCompoundDrawablesWithIntrinsicBounds(con3, null, null, null);
                    //third.setText("그리기 명상");
                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#806A3AB2")), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new RelativeSizeSpan(1.4f), start3_f, end3_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    max.setText(spannableString3);
                    break;
                case "소리":
                    third.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_4));
                    third.setCompoundDrawablesWithIntrinsicBounds(con4, null, null, null);
                    //third.setText("소리 명상");
                    spannableString3.setSpan(new ForegroundColorSpan(Color.parseColor("#8053933F")), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new RelativeSizeSpan(1.4f), start3_f, end3_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString3.setSpan(new StyleSpan(Typeface.BOLD), start3_f, end3_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    max.setText(spannableString3);
                    break;
            }
            int finalBest = best;
            third.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content[finalBest].equals("5분")) {
                        Intent intent = new Intent(getActivity(), Yun_5minutes_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest].equals("발성")) {
                        Intent intent = new Intent(getActivity(), Je_Vocalizationmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest].equals("시지각")) {
                        Intent intent = new Intent(getActivity(), In_Visualperceptionmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest].equals("그리기")) {
                        Intent intent = new Intent(getActivity(), In_Contents3Main_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest].equals("소리")) {
                        Intent intent = new Intent(getActivity(), Yun_Category_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    }
                }
            });
            int best_t = 0, bestN_t, bestC_t = 0;
            Calendar calendar = Calendar.getInstance();
            int now = calendar.get(Calendar.HOUR_OF_DAY);
            if (bestC_t % 3 != 0) {
                bestC_t = now / 3;
            } else {
                bestC_t = now / 3;
            }
            bestN_t = 0;
            for (int i = 0; i < 5; i++) {
                if (bestN_t < content_T[bestC_t][i]) {
                    bestN_t = content_T[bestC_t][i];
                    best_t = i;
                }
            }
            final String[] times = new String[]{"0~3", "3~6", "6~9", "9~12", "12~15", "15~18", "18~21", "21~24"};
            time_max.setText("현재 추천하는 명상은\n" + content[best_t] + " 명상 입니다");
            String strong4 = time_max.getText().toString();
            SpannableString spannableString4 = new SpannableString(strong4);

            String word4 = "현재 추천하는 명상";
            int start4 = strong4.indexOf(word4);
            int end4 = start4 + word4.length();

            String word4_f = content[best_t] + " 명상";
            int start4_f = strong4.indexOf(word4_f);
            int end4_f = start4_f + word4_f.length();

            spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#FFB8B8B8")), start4, end4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString4.setSpan(new RelativeSizeSpan(1.4f), start4, end4, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            switch (content[best_t]) {
                case "5분":
                    four.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_1));
                    four.setCompoundDrawablesWithIntrinsicBounds(fiveM, null, null, null);
                    //four.setText("5분 명상");
                    spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#802F6699")), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new RelativeSizeSpan(1.4f), start4_f, end4_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new StyleSpan(Typeface.BOLD), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time_max.setText(spannableString4);
                    break;
                case "발성":
                    four.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_2));
                    four.setCompoundDrawablesWithIntrinsicBounds(vocal, null, null, null);
                    //four.setText("발성 명상");
                    spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#80992F2F")), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new RelativeSizeSpan(1.4f), start4_f, end4_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new StyleSpan(Typeface.BOLD), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time_max.setText(spannableString4);
                    break;
                case "시지각":
                    four.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_3));
                    four.setCompoundDrawablesWithIntrinsicBounds(visual, null, null, null);
                    //four.setText("시지각 명상");
                    spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#80BB6008")), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new RelativeSizeSpan(1.4f), start4_f, end4_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new StyleSpan(Typeface.BOLD), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time_max.setText(spannableString4);
                    break;
                case "그리기":
                    four.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_5));
                    four.setCompoundDrawablesWithIntrinsicBounds(con3, null, null, null);
                    //four.setText("그리기 명상");
                    spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#806A3AB2")), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new RelativeSizeSpan(1.4f), start4_f, end4_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new StyleSpan(Typeface.BOLD), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time_max.setText(spannableString4);
                    break;
                case "소리":
                    four.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.btnshape_4));
                    four.setCompoundDrawablesWithIntrinsicBounds(con4, null, null, null);
                    //four.setText("소리 명상");
                    spannableString4.setSpan(new ForegroundColorSpan(Color.parseColor("#8053933F")), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new RelativeSizeSpan(1.4f), start4_f, end4_f, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString4.setSpan(new StyleSpan(Typeface.BOLD), start4_f, end4_f, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    time_max.setText(spannableString4);
                    break;
            }
            int finalBest_t = best_t;
            four.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content[finalBest_t].equals("5분")) {
                        Intent intent = new Intent(getActivity(), Yun_5minutes_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest_t].equals("발성")) {
                        Intent intent = new Intent(getActivity(), Je_Vocalizationmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest_t].equals("시지각")) {
                        Intent intent = new Intent(getActivity(), In_Visualperceptionmain_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest_t].equals("그리기")) {
                        Intent intent = new Intent(getActivity(), In_Contents3Main_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    } else if (content[finalBest_t].equals("소리")) {
                        Intent intent = new Intent(getActivity(), Yun_Category_Activity.class);
                        intent.putExtra("ID", ID);
                        intent.putExtra("IP", IP);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}