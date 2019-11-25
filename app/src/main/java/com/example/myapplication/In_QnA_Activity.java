package com.example.myapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class In_QnA_Activity extends AppCompatActivity {
    MyDBHelper myHelper;
    SQLiteDatabase sqlDB;
    String NAME, IP, ID, mJsonString;
    ListView listView;
    TextView Q, A;
    LinearLayout linear;
    ArrayList<String> Q_Array = new ArrayList<>();
    ArrayList<String> A_Array = new ArrayList<>();
    ArrayList<HashMap<String, String>> mArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.in_qna_activity);
        myHelper = new MyDBHelper(this);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        Q = findViewById(R.id.Q);
        A = findViewById(R.id.A);
        final SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.listView);
        linear = (LinearLayout) findViewById(R.id.linear);
        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.start);
        linear.startAnimation(anim);
        Q_Array.add("Q1. 명상이란 무엇인가요?");
        A_Array.add("A1. 아무런 왜곡 없는 순수한 마음 상태로 되돌아가는 것을 실천하려는 것이 명상(meditation)입니다.\n 마음을 비우고 편안한 상태가 되는 것이라고 생각하면 된답니다.");
        Q_Array.add("Q2. 명상을 하면 좋은 점이 무엇인가요?");
        A_Array.add("A2. 명상은 스트레스 관리, 학습 향상, 건강 증진, 경기력 향상, 약물중독 치료, 심리 치료, 습관 교정, 종교적 영성 개발, 자기 수양과 같은 다양한 효과를 가져옵니다.");
        Q_Array.add("Q3. 명상을 할 때마다 노래를 들으며 해야 하나요?");
        A_Array.add("A3. 스스로 편안한 상태가 될 수 있다면 조용한 환경에서 명상을 하시면 됩니다. 다만 명상에 익숙하지 않은 사람들은 편안한 노래와 함께 명상을 하는 것이 도움이 됩니다.");
        Q_Array.add("Q4. 한글 명상을 하면 좋은 점이 무엇인가요?");
        A_Array.add("A4. 한글은 신체의 모양을 보고 만들어졌습니다.\n 입 모양 뿐만 아니라 흉각, 공기의 순환 등을 고려하며 반복된 발성으로 각각의 자음, 모음에 다른 신체 부위가 활동하며 명상에 도움을 줍니다.");
        Q_Array.add("Q5. 명상을 효율적으로 하는 방법은 무엇인가요?");
        A_Array.add("A5. 습관이 중요합니다. 애플리케이션을 통해 규칙적인 명상 습관을 만들어 보다 건강한 생활환경을 유지하며 명상을 합시다.");
        Q_Array.add("Q6. 명상은 하루에 얼마나 해야 할까요?");
        A_Array.add("A6. 하루 5분만 해도 좋습니다. 명상을 오랜 시간하는 것보단 습관적으로, 편안한 마음을 가지고 심신의 안정을 유지하도록 노력해보세요.");
        HashMap<String, String> hashMap = new HashMap<>();
        mArrayList = new ArrayList<>();
        hashMap.put("Q", "Q1. 명상이란 무엇인가요?");
        mArrayList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("Q", "Q2. 명상을 하면 좋은 점이 무엇인가요?");
        mArrayList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("Q", "Q3. 명상을 할 때마다 노래를 들으며 해야 하나요?");
        mArrayList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("Q", "Q4. 한글 명상을 하면 좋은 점이 무엇인가요?");
        mArrayList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("Q", "Q5. 명상을 효율적으로 하는 방법은 무엇인가요?");
        mArrayList.add(hashMap);
        hashMap = new HashMap<>();
        hashMap.put("Q", "Q6. 명상은 하루에 얼마나 해야 할까요?");
        mArrayList.add(hashMap);
        ListAdapter adapter = new SimpleAdapter(
                In_QnA_Activity.this, mArrayList, R.layout.in_qnalist_activity,
                new String[]{"Q"},
                new int[]{R.id.q}
        );
        listView.setAdapter(adapter);
        listView.setSelection(0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Q.setText(Q_Array.get(position));
                A.setText(A_Array.get(position));
            }
        });
    }
}