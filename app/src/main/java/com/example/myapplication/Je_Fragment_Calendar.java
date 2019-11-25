package com.example.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.os.AsyncTask;
import android.graphics.Color;
import android.widget.ScrollView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.CalendarMode;

import com.example.myapplication.decorators.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;


public class Je_Fragment_Calendar extends Fragment {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    DatePicker dday;
    String ID, IP, FIRST;
    ListView schedule;
    ArrayAdapter<String> adapter;
    MaterialCalendarView CV;
    ImageView inputImage;
    ScrollView scrolView;
    TextView day_num, maditation_num;
    LinearLayout linear;
    private static final String TAG = "phpquerytest", TAG_JSON = "webnautes", TAG_NAME = "name", TAG_BIRTH = "birth", TAG_PHONE = "phone", TAG_SEX = "sex", TAG_FIRST = "first", TAG_RECENTLY = "recently";
    Bitmap getBlob;
    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.je_calendar_activity, container, false);
        schedule = (ListView) view.findViewById(R.id.schedule);
        dday = (DatePicker) view.findViewById(R.id.dpick);
        inputImage = (ImageView) view.findViewById(R.id.inputImage);
        scrolView = view.findViewById(R.id.scrollView);
        day_num = view.findViewById(R.id.day_num);
        maditation_num = view.findViewById(R.id.maditation_num);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        myHelper = new MyDBHelper(getActivity());

        try {
            Je_Fragment_Calendar.GetData1 tasks = new Je_Fragment_Calendar.GetData1();
            tasks.execute(ID);
            inputImage.setBackground(new ShapeDrawable(new OvalShape()));
            inputImage.setClipToOutline(true);
        } catch (Exception e) {
        }
        schedule.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                scrolView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        CV = view.findViewById(R.id.CV);
        CV.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                .setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        CV.addDecorators(
                new SundayDecorator(),
                new SaturdayDecorator(),
                oneDayDecorator);
        int meditation_count = 0;
        sqlDB = myHelper.getReadableDatabase();
        Cursor meditation_Cursor = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "';", null);
        while (meditation_Cursor.moveToNext()) {
            meditation_count++;
        }
        maditation_num.setText(meditation_count + "회");
        meditation_Cursor.close();
        sqlDB.close();
        int count = 0;
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor1 = sqlDB.rawQuery("SELECT *FROM attendance WHERE _id = '" + ID + "';", null);
        while (cursor1.moveToNext()) {
            count++;
        }
        day_num.setText(FIRST + "회");
        cursor1.close();
        sqlDB.close();
        String[] result = new String[count];
        int i = 0;
        sqlDB = myHelper.getReadableDatabase();
        Cursor cursor2 = sqlDB.rawQuery("SELECT *FROM attendance WHERE _id = '" + ID + "';", null);
        while (cursor2.moveToNext()) {
            result[i] = cursor2.getString(2) + "," + cursor2.getString(3) + "," + cursor2.getString(4);
            i++;
        }
        try {
            new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());
        } catch (Exception e) {

        }
        cursor2.close();
        sqlDB.close();
        CV.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                adapter.clear();
                sqlDB = myHelper.getReadableDatabase();
                Cursor cursor = sqlDB.rawQuery("SELECT *FROM calendar WHERE _id = '" + ID + "' AND year_month_day = '" + date.toString() + "';", null);
                while (cursor.moveToNext()) {
                    adapter.add(cursor.getString(3) + "\r\n");
                }
                schedule.setAdapter(adapter);
                cursor.close();
                sqlDB.close();
            }
        });
        return view;
    }

    public class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {
        String[] Time_Result;

        ApiSimulator(String[] Time_Result) {
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (int i = 0; i < Time_Result.length; i++) {
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);
                calendar.set(year, month - 1, dayy);
                CalendarDay day = CalendarDay.from(calendar);
                dates.add(day);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);
            try {
                if (getActivity().isFinishing()) {
                    return;
                }
                CV.addDecorator(new EventDecorator(Color.RED, calendarDays, getActivity()));
            } catch (Exception e) {

            }
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof In_Frame_Activity) {
            ID = ((In_Frame_Activity) getActivity()).getID();
            IP = ((In_Frame_Activity) getActivity()).getIP();
            FIRST = ((In_Frame_Activity) getActivity()).getFIRST();
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