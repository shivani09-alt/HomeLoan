package com.example.homeloan;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class DashBoard extends Fragment {

    private PieChartView pieChart;
    List<SliceValue> pieData = new ArrayList<>();
    List<GraphList> graphLists=new ArrayList<>();
    int color;
    View  view;
    TextView nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_dash_board, container, false);
        pieChart = view.findViewById(R.id.piechart);
        nodata = view.findViewById(R.id.nodata);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        pieData = new ArrayList<>();
        graphLists=new ArrayList<>();
        DataBaseHelper dataBaseHelper=new DataBaseHelper(getActivity());
        graphLists=dataBaseHelper.fetchStateCount();
        Random rnd = new Random();


        for(int i=0;i<graphLists.size();i++){
            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            pieData.add(new SliceValue(Integer.parseInt(graphLists.get(i).getCount()), color).setLabel(graphLists.get(i).getStateName()+" "+graphLists.get(i).getCount()+"%"));

        }
        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);
        pieChart.setPieChartData(pieChartData);
        if(graphLists.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }
        else{
            nodata.setVisibility(View.GONE);
        }
    }
}