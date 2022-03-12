package com.example.homeloan;




import android.graphics.Color;
import android.os.Bundle;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;



public class ShowUserDetails extends AppCompatActivity {
    TextInputEditText fName,lName,contact,projectName,flatDetails,propertCost,loanRequirements,state,city;
    String id;
    SliderView sliderView;
    ArrayList<User> arrayList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_show_user_details);
        state=findViewById(R.id.state);
        city=findViewById(R.id.city);
        fName=findViewById(R.id.Fname);
        lName=findViewById(R.id.Lname);
        contact=findViewById(R.id.phoneNumber);
        projectName=findViewById(R.id.projectName);
        flatDetails=findViewById(R.id.flatDetails);
        propertCost=findViewById(R.id.propertCost);
        loanRequirements=findViewById(R.id.loanRequirements);
        id=getIntent().getStringExtra("id");
       sliderView = findViewById(R.id.imageSlider);


        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.FOCUS_FORWARD);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }
    private void display()
    {
    DataBaseHelper dataBaseHelper=new DataBaseHelper(getApplicationContext());
    arrayList=dataBaseHelper.getIndividualUserDetails(id);
        fName.setText(arrayList.get(0).getfName());
        lName.setText(arrayList.get(0).getlName());
        contact.setText(arrayList.get(0).getContact());
        projectName.setText(arrayList.get(0).getProjectName());
        flatDetails.setText(arrayList.get(0).getFlatDetails());
        propertCost.setText(arrayList.get(0).getPropertyCost());
        loanRequirements.setText(arrayList.get(0).getLoadRequirement());
        state.setText(arrayList.get(0).getState());
        city.setText(arrayList.get(0).getCity());
        ArrayList<byte[]> list=dataBaseHelper.getImageList(id);
        SliderAdapterExample adapter = new SliderAdapterExample(this,list);

        sliderView.setSliderAdapter(adapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        display();
    }
}
