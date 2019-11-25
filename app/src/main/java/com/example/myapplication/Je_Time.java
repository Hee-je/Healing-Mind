package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

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
import java.util.List;

public class Je_Time extends Fragment {
    String ID, IP, mJsonString;
    int[] times_arr = new int[8];
    int[] times_arr2 = new int[8];
    String[] content = new String[8];
    LinearLayout linear;
    BarChart barChart;
    TextView A, B, C, D, E, F, G, H, people, Nodata;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_TIME = "time", TAG_CONTENT = "content";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Je_Time() {
    }

    public static Je_Time newInstance(String param1, String param2) {
        Je_Time fragment = new Je_Time();
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
        View view = inflater.inflate(R.layout.je_time, container, false);
        barChart = (BarChart) view.findViewById(R.id.barchart);
        Nodata = (TextView) view.findViewById(R.id.Nodata);
        A = (TextView) view.findViewById(R.id.A);
        B = (TextView) view.findViewById(R.id.B);
        C = (TextView) view.findViewById(R.id.C);
        D = (TextView) view.findViewById(R.id.D);
        E = (TextView) view.findViewById(R.id.E);
        F = (TextView) view.findViewById(R.id.F);
        G = (TextView) view.findViewById(R.id.G);
        H = (TextView) view.findViewById(R.id.H);
        people = (TextView) view.findViewById(R.id.people);
        people.setText("< 전체 이용자 통계 >");
        GetData task = new GetData();
        task.execute(ID);
        GetData2 task2 = new GetData2();
        task2.execute();
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
            String searchKeyword = params[0];
            String serverURL = "http://" + IP + "/chart_time_user.php";
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
            String time_str;
            Integer time_int;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                time_str = item.getString(TAG_TIME);
                time_str = time_str.substring(0, 2);
                time_int = Integer.parseInt(time_str);
                if (time_int >= 21)  // 21시 ~ 23시 59분
                {
                    times_arr[7]++;
                } else if (time_int >= 18) // 18시 ~ 20시 59분
                {
                    times_arr[6]++;
                } else if (time_int >= 15) {
                    times_arr[5]++;
                } else if (time_int >= 12) {
                    times_arr[4]++;
                } else if (time_int >= 9) {
                    times_arr[3]++;
                } else if (time_int >= 6) {
                    times_arr[2]++;
                } else if (time_int >= 3) {
                    times_arr[1]++;
                } else {
                    times_arr[0]++;
                }
            }
            List<BarEntry> entries = new ArrayList<BarEntry>();
            for (int i = 0; i < 8; i++) {
                entries.add(new BarEntry(i, times_arr[i]));
            }
            if (times_arr[0] + times_arr[1] + times_arr[2] + times_arr[3] + times_arr[4] + times_arr[5] + times_arr[6] + times_arr[7] == 0) {
                Nodata.setVisibility(View.VISIBLE);
                barChart.setVisibility(View.GONE);
            } else {
                BarDataSet barDataSet = new BarDataSet(entries, "");
                barDataSet.setBarBorderWidth(0.4f);
                barDataSet.setColors(new int[]{Color.argb(130, 68, 157, 239), Color.argb(130, 239, 68, 68), Color.argb(130, 243, 174, 27), Color.argb(130, 162, 118, 235),
                        Color.argb(130, 112, 198, 86), Color.argb(130, 231, 235, 118), Color.argb(130, 235, 118, 165), Color.argb(130, 68, 239, 199)});
                barDataSet.setValueTextSize(12);
                BarData barData = new BarData(barDataSet);
                XAxis xAxis = barChart.getXAxis();
                barChart.getAxisRight().setDrawGridLines(false);
                barChart.getAxisLeft().setDrawGridLines(false);
                barChart.getXAxis().setDrawGridLines(false);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                final String[] times = new String[]{"0~3", "3~6", "6~9", "9~12", "12~15", "15~18", "18~21", "21~24"};
                IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(times);
                xAxis.setGranularity(1f);
                xAxis.setValueFormatter(formatter);
                Description des = barChart.getDescription();
                des.setEnabled(false);
                Legend leg = barChart.getLegend();
                leg.setEnabled(false);
                barChart.setData(barData);
                barChart.setFitBars(false);
                barChart.animateXY(1000, 1000);
                barChart.invalidate();
            }
        } catch (JSONException e) {
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
            String serverURL = "http://" + IP + "/chart_time.php";
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

    private void showResult2() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);
            String time_str2;
            Integer time_int2;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                time_str2 = item.getString(TAG_TIME);
                time_str2 = time_str2.substring(0, 2);
                time_int2 = Integer.parseInt(time_str2);
                if (time_int2 >= 21)  // 21시 ~ 23시 59분
                {
                    times_arr2[7]++;
                } else if (time_int2 >= 18) // 18시 ~ 20시 59분
                {
                    times_arr2[6]++;
                } else if (time_int2 >= 15) {
                    times_arr2[5]++;
                } else if (time_int2 >= 12) {
                    times_arr2[4]++;
                } else if (time_int2 >= 9) {
                    times_arr2[3]++;
                } else if (time_int2 >= 6) {
                    times_arr2[2]++;
                } else if (time_int2 >= 3) {
                    times_arr2[1]++;
                } else {
                    times_arr2[0]++;
                }
            }
            A.setText(String.valueOf(times_arr2[0]));
            B.setText(String.valueOf(times_arr2[1]));
            C.setText(String.valueOf(times_arr2[2]));
            D.setText(String.valueOf(times_arr2[3]));
            E.setText(String.valueOf(times_arr2[4]));
            F.setText(String.valueOf(times_arr2[5]));
            G.setText(String.valueOf(times_arr2[6]));
            H.setText(String.valueOf(times_arr2[7]));
        } catch (JSONException e) {
        }
    }
}