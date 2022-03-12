package com.example.homeloan;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class SliderImageAdapter extends PagerAdapter {
    private ArrayList imageArray;
    private LayoutInflater inflater;
    private Context context;
    String currentPositionImage;

    public SliderImageAdapter( Context context, ArrayList imageArray, String currentPositionImage) {
        this.context = context;
        this.imageArray=imageArray;
        inflater = LayoutInflater.from(context);
        this.currentPositionImage=currentPositionImage;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return imageArray.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.display_full_screen_image_gallery, view, false);
        final ImageView imageView =  imageLayout
                .findViewById(R.id.image);

            Picasso.with(context)
                    .load(new File(imageArray.get(position).toString()))
                    .into(imageView);



            view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}