package com.example.myapplication;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class VisualPerceptionPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public VisualPerceptionPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                in_jaum jaum = new in_jaum();
                return jaum;
            case 1:
                in_moum moum = new in_moum();
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