package com.example.homeloan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class SliderPreview extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    ViewPager viewPager;
    ArrayList<String> mArrayUri = new ArrayList<String>() ;
    String currentPosition;
    SliderImageAdapter adapter;
    int imagesPos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_preview);
        imagesPos=getIntent().getIntExtra("positionImages",0);
        viewPager=findViewById(R.id.viewpager);
        mArrayUri=  getIntent().getStringArrayListExtra("ARRAYLIST");

        adapter =new SliderImageAdapter(getApplicationContext(),mArrayUri,currentPosition);
        viewPager.addOnPageChangeListener( this);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(imagesPos,false);






    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}