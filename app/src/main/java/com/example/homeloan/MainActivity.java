    package com.example.homeloan;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;


    public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabAdapter adapter;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewpager);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        adapter=new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new LeadGeneration(), "LeadGeneration");
        adapter.addFragment(new LeadList(), "LeadList");
        adapter.addFragment(new DashBoard(), "Dashboard");
        viewPager.setAdapter(adapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.generation:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.list:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.dashboard:
                    viewPager.setCurrentItem(2);
                    break;
            }
            return false;
        });
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onPageSelected(int position) {
                Fragment fragment=adapter.getFragment(position);
                if (position ==2 && fragment != null)
                {
                    fragment.onResume();
                }
                else if (position ==1 && fragment != null)
                {
                    fragment.onResume();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }
}