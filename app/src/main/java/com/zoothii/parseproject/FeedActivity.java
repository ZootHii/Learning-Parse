package com.zoothii.parseproject;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.zoothii.parseproject.ui.main.SectionsPagerAdapter;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        tabs.getTabAt(0).setCustomView(R.layout.icon_view);
        tabs.getTabAt(0).setText("");
        tabs.getTabAt(1).setCustomView(R.layout.icon_view1);
        tabs.getTabAt(1).setText("");

        tabs.getTabAt(2).setCustomView(R.layout.icon_view2);
        tabs.getTabAt(2).setText("");

    }
}