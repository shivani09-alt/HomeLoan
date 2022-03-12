package com.example.homeloan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class ImagesRecyclerView extends RecyclerView.Adapter {
    Context context;
    ArrayList<String>  array;
    EventListener eventListener;
    interface  EventListener{
        void deleteImage(int position);
    }
    ImagesRecyclerView(Context context, ArrayList<String> array,  EventListener eventListener){
     this.context=context;
     this.array=array;
     this.eventListener=eventListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        // Inflate the layout

        View view
                = inflater
                .inflate(R.layout.imageview_layout,
                        parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    ViewHolderClass viewHolderClass=(ViewHolderClass) holder;
        Picasso.with(context)
                .load(new File(array.get(position)))
                .resize(0,120)
                .into(viewHolderClass.imageView);
        viewHolderClass.albumDelete.setOnClickListener(view -> {
            eventListener.deleteImage(position);
        });
        viewHolderClass.imageView.setOnClickListener(view -> {
            Intent intent=new Intent(context,SliderPreview.class);
            intent.putStringArrayListExtra("ARRAYLIST",array);
            intent.putExtra("positionImages",position);
            intent.putExtra("type","recylerview");
            context.startActivity(intent);
        });
        viewHolderClass.gridImage.setVisibility(View.VISIBLE);
        viewHolderClass.albumDelete.setVisibility(View.VISIBLE);

    }

    @Override
    public int getItemCount() {
        return array.size();
    }
    public class ViewHolderClass extends RecyclerView.ViewHolder{
        ImageView imageView,albumDelete;
        RelativeLayout gridImage;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image);
            albumDelete=itemView.findViewById(R.id.albumDelete);

            gridImage=itemView.findViewById(R.id.gridImage);
        }
    }
}
