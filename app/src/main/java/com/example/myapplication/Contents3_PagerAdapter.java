package com.example.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Contents3_PagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public Contents3_PagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                in_jaum3 jaum = new in_jaum3();
                return jaum;
            case 1:
                in_moum3 moum = new in_moum3();
                return moum;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}