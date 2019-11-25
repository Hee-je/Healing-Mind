package com.example.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class VocalizationPagerAdapter extends FragmentStatePagerAdapter {
    private int mPageCount;

    public VocalizationPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                jaum jaum = new jaum();
                return jaum;
            case 1:
                moum moum = new moum();
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