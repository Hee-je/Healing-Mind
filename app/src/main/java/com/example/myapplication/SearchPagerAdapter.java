package com.example.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SearchPagerAdapter extends FragmentStatePagerAdapter {

    private int mPageCount;

    public SearchPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.mPageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Je_IDsearch_Fragment Je_IDsearch_Fragment = new Je_IDsearch_Fragment();
                return Je_IDsearch_Fragment;
            case 1:
                Je_PWsearch_Fragment Je_PWsearch_Fragment = new Je_PWsearch_Fragment();
                return Je_PWsearch_Fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mPageCount;
    }
}
