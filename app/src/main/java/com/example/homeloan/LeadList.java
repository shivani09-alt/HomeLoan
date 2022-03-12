package com.example.homeloan;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.ArrayList;

public class LeadList extends Fragment implements LeadListAdpter.EventListener {
    RecyclerView recyclerView;
    LeadListAdpter adapter;
    ArrayList<User> arrayList;
    SwipeRefreshLayout swipeRefreshLayout;
    View view;
    TextView nodata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view= inflater.inflate(R.layout.fragment_lead_list, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        nodata = view.findViewById(R.id.nodata);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeRefreshLayout=view.findViewById(R.id.refreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            DataBaseHelper dataBaseHelper1 =new DataBaseHelper(getActivity());
            arrayList= dataBaseHelper1.getListOfUser();
            adapter=new LeadListAdpter(getActivity(),arrayList,LeadList.this);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
        });
        return view;
    }

    @Override
    public void onDelete(int position) {
        arrayList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    @Override
    public void onResume() {
        super.onResume();
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        arrayList=new ArrayList<>();
        DataBaseHelper dataBaseHelper=new DataBaseHelper(getActivity());
        arrayList=dataBaseHelper.getListOfUser();
        adapter=new LeadListAdpter(getActivity(),arrayList,LeadList.this);
        recyclerView.setAdapter(adapter);
        if(arrayList.size()==0){
            nodata.setVisibility(View.VISIBLE);
        }
        else{
            nodata.setVisibility(View.GONE);
        }
    }
}