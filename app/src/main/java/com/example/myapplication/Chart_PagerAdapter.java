package com.example.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Chart_PagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public Chart_PagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Je_Month je_month = new Je_Month();
                return je_month;
            case 1:
                Je_Time je_time = new Je_Time();
                return je_time;
            case 2:
                Je_Recommend je_recommend = new Je_Recommend();
                return je_recommend;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}