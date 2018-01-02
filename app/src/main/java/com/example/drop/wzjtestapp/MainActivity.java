package com.example.drop.wzjtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private OrdersListAdapter adapter;
    @Override
    public void initParams(Bundle params) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState, View view) {
        recyclerView = find(R.id.recyclerView);
    }

    @Override
    public void setOnClickListener() {

    }

    @Override
    public void initData(Context mContext) {

    }

    @Override
    public void widgetClick(View v) {

    }
}
