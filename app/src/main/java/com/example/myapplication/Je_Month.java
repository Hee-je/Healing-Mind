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

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

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

public class Je_Month extends Fragment {
    String ID, IP, mJsonString;
    PieChart pieChart;
    LinearLayout linear;
    String[] num = new String[5];
    Integer[] num_int = new Integer[5];
    String[] content = new String[5];
    String[] num2 = new String[5];
    Integer[] num_int2 = new Integer[5];
    String[] content2 = new String[5];
    TextView nodata, min5, voice, see, draw, sound, people;
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_NUM = "num", TAG_CONTENT = "content";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public Je_Month() {
    }

    public static Je_Month newInstance(String param1, String param2) {
        Je_Month fragment = new Je_Month();
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
        View view = inflater.inflate(R.layout.je_month, container, false);
        pieChart = (PieChart) view.findViewById(R.id.piechart);
        nodata = (TextView) view.findViewById(R.id.Nodata);

        min5 = (TextView) view.findViewById(R.id.min5);
        voice = (TextView) view.findViewById(R.id.voice);
        draw = (TextView) view.findViewById(R.id.draw);
        see = (TextView) view.findViewById(R.id.see);
        sound = (TextView) view.findViewById(R.id.sound);
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
            String serverURL = "http://" + IP + "/chart_month_user.php";
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
            content[0] = "5분";
            content[1] = "발성";
            content[2] = "시지각";
            content[3] = "그리기";
            content[4] = "소리";
            for (int i = 0; i < 5; i++) {
                num[i] = "0";
                num_int[i] = Integer.parseInt(num[i]);
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                switch (item.getString(TAG_CONTENT)) {
                    case "5분":
                        num[0] = item.getString(TAG_NUM);
                        num_int[0] = Integer.parseInt(num[0]);
                        break;
                    case "발성":
                        num[1] = item.getString(TAG_NUM);
                        num_int[1] = Integer.parseInt(num[1]);
                        break;
                    case "시지각":
                        num[2] = item.getString(TAG_NUM);
                        num_int[2] = Integer.parseInt(num[2]);
                        break;
                    case "그리기":
                        num[3] = item.getString(TAG_NUM);
                        num_int[3] = Integer.parseInt(num[3]);
                        break;
                    case "소리":
                        num[4] = item.getString(TAG_NUM);
                        num_int[4] = Integer.parseInt(num[4]);
                        break;
                }
            }
            pieChart.setUsePercentValues(true);
            pieChart.getDescription().setEnabled(false);
            pieChart.setExtraOffsets(10, 10, 10, 10);
            pieChart.setDragDecelerationFrictionCoef(0.95f);
            pieChart.setDrawEntryLabels(false);
            pieChart.setDrawHoleEnabled(false);
            pieChart.setHoleColor(Color.WHITE);
            pieChart.setTransparentCircleRadius(61f);
            Legend legend = pieChart.getLegend();
            legend.setFormSize(20f);
            legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
            ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
            if ((num_int[0] + num_int[1] + num_int[2] + num_int[3] + num_int[4]) == 0) {
                nodata.setVisibility(View.VISIBLE);
                pieChart.setVisibility(View.GONE);
            } else {
                yValues.add(new PieEntry(num_int[0], content[0]));
                yValues.add(new PieEntry(num_int[1], content[1]));
                yValues.add(new PieEntry(num_int[2], content[2]));
                yValues.add(new PieEntry(num_int[3], content[3]));
                yValues.add(new PieEntry(num_int[4], content[4]));
                pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic); //애니메이션
                PieDataSet dataSet = new PieDataSet(yValues, "");
                dataSet.setSliceSpace(3f);
                dataSet.setSelectionShift(35f);
                dataSet.setColors(new int[]{Color.argb(130, 68, 157, 239), Color.argb(130, 239, 68, 68), Color.argb(130, 243, 174, 27), Color.argb(130, 162, 118, 235), Color.argb(130, 112, 198, 86)});
                dataSet.setValueLinePart2Length(0.8f);
                dataSet.setValueLineColor(Color.rgb(60, 60, 60));
                dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
                PieData data = new PieData((dataSet));
                data.setValueTextSize(12);
                data.setValueTextColor(Color.rgb(60, 60, 60));
                pieChart.setData(data);
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
            String serverURL = "http://" + IP + "/chart_month.php";
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
            int sum = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                num2[i] = item.getString(TAG_NUM);
                content2[i] = item.getString(TAG_CONTENT);
                num_int2[i] = Integer.parseInt(num2[i]);
                sum += num_int2[i];
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                double dou = (double) ((double) num_int2[i] / (double) sum) * 100;
                switch (content2[i]) {
                    case "5분":
                        min5.setText((Math.round(dou * 100) / 100.0) + "%\n(" + num2[i] + ")");
                        break;
                    case "그리기":
                        draw.setText((Math.round(dou * 100) / 100.0) + "%\n(" + num2[i] + ")");
                        break;
                    case "발성":
                        voice.setText((Math.round(dou * 100) / 100.0) + "%\n(" + num2[i] + ")");
                        break;
                    case "소리":
                        sound.setText((Math.round(dou * 100) / 100.0) + "%\n(" + num2[i] + ")");
                        break;
                    case "시지각":
                        see.setText((Math.round(dou * 100) / 100.0) + "%\n(" + num2[i] + ")");
                        break;
                }
            }
        } catch (JSONException e) {
        }
    }
}