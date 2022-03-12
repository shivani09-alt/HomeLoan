package com.example.homeloan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapterExample extends
        SliderViewAdapter<SliderAdapterExample.SliderAdapterVH> {

    private Context context;
    private ArrayList<byte[]> mSliderItems;

    public SliderAdapterExample(Context context, ArrayList<byte[]> mSliderItems) {
        this.context = context;
        this.mSliderItems=mSliderItems;
    }

    public void renewItems(ArrayList<byte[]> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }



    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageview_layout, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {


        Bitmap bitmapImage = BitmapFactory.decodeByteArray(mSliderItems.get(position), 0, mSliderItems.get(position).length);
        viewHolder.imageViewBackground.setImageBitmap(bitmapImage);
        viewHolder.imageViewBackground.setVisibility(View.VISIBLE);
    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.slideImage);

            this.itemView = itemView;
        }
    }

}