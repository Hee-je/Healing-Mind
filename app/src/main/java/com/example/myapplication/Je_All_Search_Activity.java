package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import java.util.Random;

public class Je_All_Search_Activity extends AppCompatActivity implements Je_IDsearch_Fragment.OnFragmentInteractionListener, Je_PWsearch_Fragment.OnFragmentInteractionListener {
    EditText id;
    String ID, NAME, EMAIL, IP, mJsonString;

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private SearchPagerAdapter mContentPagerAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_all_search_activity);
        Intent intent = getIntent();
        IP = intent.getStringExtra("IP");
        mContext = getApplicationContext();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("ID"));
        mTabLayout.addTab(mTabLayout.newTab().setText("PW"));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mContentPagerAdapter = new SearchPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
        mViewPager.setAdapter(mContentPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public String getData() {
        return IP;
    }
}
