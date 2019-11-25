package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class Je_Vocalizationmain_Activity extends AppCompatActivity implements jaum.OnFragmentInteractionListener, moum.OnFragmentInteractionListener {
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    String ID, IP;
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private VocalizationPagerAdapter mContentPagerAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.je_vocalizationmain_activity);
        Intent intent = getIntent();
        ID = intent.getStringExtra("ID");
        IP = intent.getStringExtra("IP");
        mContext = getApplicationContext();
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.addTab(mTabLayout.newTab().setText("자음"));
        mTabLayout.addTab(mTabLayout.newTab().setText("모음"));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mContentPagerAdapter = new VocalizationPagerAdapter(getSupportFragmentManager(), mTabLayout.getTabCount());
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
        return ID;
    }

    public String getData2() {
        return IP;
    }
}