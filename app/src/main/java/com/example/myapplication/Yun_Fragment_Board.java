package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

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
import java.util.Timer;
import java.util.TimerTask;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Yun_Fragment_Board extends Fragment {

    String NAME, IP, ID, mJsonString;
    ListView listView;
    LinearLayout linear;
    ArrayList<String> board_memo = new ArrayList<>();
    ArrayList<String> board_id = new ArrayList<>();
    ArrayList<String> board_date = new ArrayList<>();
    ArrayList<String> board_name = new ArrayList<>();
    ArrayList<String> board_title = new ArrayList<>();
    ArrayList<String> board_num = new ArrayList<>();
    ArrayList<HashMap<String, String>> mArrayList;
    ArrayAdapter<String> adapter;
    Integer list_num = 0, list_start = 0, list_point = 7;
    boolean lastItemVisibleFlag = false, first = true;
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_NAME = "name", TAG_MEMO = "memo", TAG_DATE = "date", TAG_TITLE = "title", TAG_NUM = "num", TAG_ID = "id";
    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_boardtool_activity, container, false);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
        progressBar = (ProgressBar) view.findViewById(R.id.progressbar);
        Yun_Fragment_Board.GetData task = new Yun_Fragment_Board.GetData();
        mArrayList = new ArrayList<>();
        task.execute();
        FloatingActionButton plus = (FloatingActionButton) view.findViewById(R.id.plus);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upload = new Intent(getActivity(), Yun_BoardUpload_Activity.class);
                upload.putExtra("NAME", NAME);
                upload.putExtra("ID", ID);
                upload.putExtra("IP", IP);
                startActivity(upload);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent show = new Intent(getActivity(), Yun_ShowBoard_Activity.class);
                show.putExtra("NAME", board_name.get(position));
                show.putExtra("TITLE", board_title.get(position));
                show.putExtra("DATE", board_date.get(position));
                show.putExtra("MEMO", board_memo.get(position));
                show.putExtra("board_NAME", board_name.get(position));
                show.putExtra("NAME", NAME);
                show.putExtra("ID", ID);
                show.putExtra("IP", IP);
                show.putExtra("NUM", board_num.get(position));
                String check = "no";
                if (ID.equals(board_id.get(position))) {
                    check = "yes";
                }
                show.putExtra("CHECK", check);
                startActivity(show);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag) {
                    final ProgressDialog progressDialog2 = new ProgressDialog(
                            getActivity());
                    progressDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog2.setMessage("Please Wait");
                    progressDialog2.show();
                    showResult();
                    new Timer().schedule(new TimerTask() {
                        public void run() {
                            progressDialog2.dismiss();
                        }
                    }, 1000);
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
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
            String serverURL = "http://" + IP + "/print.php";
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
            if (first) {
                list_start = jsonArray.length();
            }
            if (list_start < 15) {
                list_num = 0;
            } else {
                list_num = list_start - 15;
            }
            if (list_num < 0) {
                list_num = 0;
            }
            for (int i = list_start - 1; i >= list_num; i--) {
                JSONObject item = jsonArray.getJSONObject(i);
                board_name.add(item.getString(TAG_NAME));
                board_num.add(item.getString(TAG_NUM));
                board_id.add(item.getString(TAG_ID));
                board_date.add(item.getString(TAG_DATE));
                board_title.add(item.getString(TAG_TITLE));
                board_memo.add(item.getString(TAG_MEMO));
                String names = "작성자 : " + item.getString(TAG_NAME);
                String dates = "작성일 : " + item.getString(TAG_DATE);
                String titles = item.getString(TAG_TITLE);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(TAG_NAME, names);
                hashMap.put(TAG_TITLE, titles);
                hashMap.put(TAG_DATE, dates);
                mArrayList.add(hashMap);
            }
            list_start -= 15;
            if (list_start < 0) list_start = -1;
            ListAdapter adapter = new SimpleAdapter(
                    getActivity(), mArrayList, R.layout.yun_board_list_activity,
                    new String[]{TAG_NAME, TAG_DATE, TAG_TITLE},
                    new int[]{R.id.borad_list_name, R.id.borad_list_date, R.id.borad_list_title}
            );
            listView.setAdapter(adapter);
            if (first) {
                listView.setSelection(0);
                first = false;
            } else {
                listView.setSelection(list_point);
                list_point += 15;
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        first = true;
        list_point = 0;
        board_memo = new ArrayList<>();
        board_title = new ArrayList<>();
        board_date = new ArrayList<>();
        board_name = new ArrayList<>();
        board_id = new ArrayList<>();
        board_num = new ArrayList<>();
        Yun_Fragment_Board.GetData task = new Yun_Fragment_Board.GetData();
        mArrayList = new ArrayList<>();
        task.execute();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof In_Frame_Activity) {
            ID = ((In_Frame_Activity) getActivity()).getID();
            IP = ((In_Frame_Activity) getActivity()).getIP();
            NAME = ((In_Frame_Activity) getActivity()).getNAME();
        }
    }
}
