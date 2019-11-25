package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class jaum extends Fragment {
    Button[] numButtons = new Button[14];
    Integer[] numBtnIds = {R.id.r, R.id.s, R.id.e, R.id.f, R.id.a, R.id.q, R.id.t, R.id.d, R.id.w, R.id.c, R.id.z, R.id.x, R.id.v, R.id.g};
    String ID, IP, letter;
    LinearLayout linear;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public jaum() {
    }

    public static jaum newInstance(String param1, String param2) {
        jaum fragment = new jaum();
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
                    Intent intent = new Intent(getActivity(), Je_Vocalization_Activity.class);
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
        if (getActivity() != null && getActivity() instanceof Je_Vocalizationmain_Activity) {
            ID = ((Je_Vocalizationmain_Activity) getActivity()).getData();
            IP = ((Je_Vocalizationmain_Activity) getActivity()).getData2();
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