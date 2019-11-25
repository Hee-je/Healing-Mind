package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import static com.facebook.FacebookSdk.getApplicationContext;

public class in_jaum3 extends Fragment {
    Button[] numButtons = new Button[14];
    Integer[] numBtnIds = {R.id.r, R.id.s, R.id.e, R.id.f, R.id.a, R.id.q, R.id.t, R.id.d, R.id.w, R.id.c, R.id.z, R.id.x, R.id.v, R.id.g};
    LinearLayout linear;
    String ID, IP, letter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public in_jaum3() {
    }

    public static in_jaum3 newInstance(String param1, String param2) {
        in_jaum3 fragment = new in_jaum3();
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
        View view = inflater.inflate(R.layout.fragment_jaum, container, false);
        for (int i = 0; i < numBtnIds.length; i++) {
            numButtons[i] = (Button) view.findViewById(numBtnIds[i]);
        }
        for (int i = 0; i < numBtnIds.length; i++) {
            final int index;
            index = i;
            numButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    letter = numButtons[index].getText().toString();
                    Intent intent = new Intent(getActivity(), In_Content3Select_Activity.class);
                    intent.putExtra("letter", letter);
                    intent.putExtra("ID", ID);
                    intent.putExtra("IP", IP);
                    startActivity(intent);
                }
            });
        }
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
        if (getActivity() != null && getActivity() instanceof In_Contents3Main_Activity) {
            ID = ((In_Contents3Main_Activity) getActivity()).getData();
            IP = ((In_Contents3Main_Activity) getActivity()).getData2();
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
}