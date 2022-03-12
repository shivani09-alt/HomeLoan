package com.example.homeloan;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class LeadListAdpter extends RecyclerView.Adapter {
    ArrayList<User> arrayList;
    NavController controller;
    Context context;
    EventListener eventListener;
    interface EventListener{
        void onDelete(int position);
    }
    public LeadListAdpter(Context context, ArrayList<User> arrayList,EventListener eventListener){
        this.arrayList=arrayList;
        this.context=context;
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
                .inflate(R.layout.user_layout,
                        parent, false);
        return new ViewUserViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
      ViewUserViewHolder viewHolder=(ViewUserViewHolder) holder;
        Random random = new Random(); int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        viewHolder.userNameLetter.setText(arrayList.get(position).getfName().toUpperCase().charAt(0)+""+arrayList.get(position).getlName().toUpperCase().charAt(0));
        viewHolder.userName.setText(arrayList.get(position).getfName()+" "+arrayList.get(position).getlName());
        viewHolder.userEmail.setText(arrayList.get(position).getContact());
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setCornerRadii(new float[]{0, 0, 0, 0, 0, 0, 0, 0});
        shape.setColor(color);
        viewHolder.userNameLetter.setBackground(shape);

        viewHolder.delete.setOnClickListener(view -> {
          alertDialog(position);

        });

        viewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ShowUserDetails.class);
                intent.putExtra("id",arrayList.get(position).getId());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewUserViewHolder extends RecyclerView.ViewHolder{
         TextView userNameLetter,userName,userEmail;
         ImageView delete,view;
         RelativeLayout mainLayout;
        public ViewUserViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameLetter=itemView.findViewById(R.id.userNameLetter);
            userName=itemView.findViewById(R.id.userName);
            userEmail=itemView.findViewById(R.id.userEmail);
            mainLayout=itemView.findViewById(R.id.mainLayout);
            delete=itemView.findViewById(R.id.delete);
            view=itemView.findViewById(R.id.view);

        }
    }
    private void alertDialog(int position){
        final AlertDialog alertDialog;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = Objects.requireNonNull((FragmentActivity)context).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView yes=dialogView.findViewById(R.id.yesText);
        TextView no=dialogView.findViewById(R.id.noText);
        TextView msg=dialogView.findViewById(R.id.msg);
        msg.setText("Are you sure you want to delete");
        alertDialog = dialogBuilder.create();
        no.setOnClickListener(v -> alertDialog.dismiss());
        yes.setText("Delete");
        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        yes.setOnClickListener(v -> {

            alertDialog.dismiss(); //use to dismiss the dialog box
            DataBaseHelper dataBaseHelper=new DataBaseHelper(context);
            dataBaseHelper.deleteUser(arrayList.get(position).getId());
            eventListener.onDelete(position);



        });
        no.setOnClickListener(v -> {
            alertDialog.dismiss(); //use to dismiss the dialog box
        });
        alertDialog.show();

    }

}
