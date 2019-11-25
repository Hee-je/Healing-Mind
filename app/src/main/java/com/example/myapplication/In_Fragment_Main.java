package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import static com.facebook.FacebookSdk.getApplicationContext;

public class In_Fragment_Main extends Fragment {
    Button min5, vocal, visual, sound, match, interest, line;
    String ID, IP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.in_main_activity, container, false);
        min5 = (Button) view.findViewById(R.id.first);
        vocal = (Button) view.findViewById(R.id.second);
        visual = (Button) view.findViewById(R.id.third);
        sound = (Button) view.findViewById(R.id.four);
        line = (Button) view.findViewById(R.id.five);
        match = (Button) view.findViewById(R.id.six);
        interest = (Button) view.findViewById(R.id.seven);
        min5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Yun_5minutes_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        vocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Je_Vocalizationmain_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        visual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), In_Visualperceptionmain_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), In_Contents3Main_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Yun_Category_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Je_Recently_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        interest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Je_BookMark_Activity.class);
                intent.putExtra("ID", ID);
                intent.putExtra("IP", IP);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof In_Frame_Activity) {
            ID = ((In_Frame_Activity) getActivity()).getID();
            IP = ((In_Frame_Activity) getActivity()).getIP();
        }
    }
}


