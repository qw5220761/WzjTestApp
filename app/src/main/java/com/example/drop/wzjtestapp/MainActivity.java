package com.example.drop.wzjtestapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;

import com.example.drop.wzjtestapp.adapter.OrdersListAdapter;
import com.example.drop.wzjtestapp.database.bean.TestData;
import com.example.drop.wzjtestapp.i.AppBarStateChangeListener;
import com.example.drop.wzjtestapp.utils.LogUtil;
import com.example.drop.wzjtestapp.views.ptr.PtrDefaultFrameLayout;
import com.example.drop.wzjtestapp.views.recycler.wrapper.LoadMoreWrapper;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class MainActivity extends BaseActivity  implements LoadMoreWrapper.OnLoadMoreListener {
    private RecyclerView recyclerView;
    private OrdersListAdapter adapter;
    private AppBarLayout appBarLayout;
    private PtrDefaultFrameLayout ptrFrameLayout;
    private View loadMoreLayout;
    private boolean expand = false;
    private RadioGroup radioGroup;
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
        appBarLayout = find(R.id.appBarLayout);
        radioGroup = find(R.id.radioGroup);
        ptrFrameLayout = find(R.id.ptrFrameLayout);
        ptrFrameLayout.buildPtr(new PtrHandler() {

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

                LogUtil.i("检查是否可以刷新");
                boolean flag = PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int index = manager.findFirstVisibleItemPosition();
                View childAt = recyclerView.getChildAt(0);
                boolean indexTop = false;
                if (childAt == null || (index == 0 && childAt.getTop() == 0)) {
                    indexTop = true;
                }
                return indexTop && flag&&expand; // 可以下拉刷新
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrFrameLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogUtil.i("隐藏刷新");
                        ptrFrameLayout.refreshComplete();
                    }
                }, 500);
            }
        });
        ptrFrameLayout.setPullToRefresh(false );
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new OrdersListAdapter(this, new ArrayList<TestData>());
        recyclerView.setAdapter(adapter.getWrapperAdapter());
        loadMoreLayout = LayoutInflater.from(this).inflate(R.layout.load_layout, recyclerView, false);
        adapter.setOnLoadMoreListener(this);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                LogUtil.e("STATE", state.name());
                if( state == State.EXPANDED ) {
                    //展开状态
                    expand = true;
                }else if(state == State.COLLAPSED){
                    //折叠状态
                    expand = false;

                }else {
                    //中间状态
                    expand = false;

                }
            }
        });
    }

    @Override
    public void setOnClickListener() {
        radioGroup.setOnClickListener(this);
    }

    @Override
    public void initData(Context mContext) {
        ArrayList<TestData> list = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            TestData data1 = new TestData();
            data1.setImage("http://tva4.sinaimg.cn/crop.0.0.640.640.1024/9632347bjw8f01xh8w9dkj20hs0hsaah.jpg");
            data1.setName("测试");
            list.add(data1);
        }
        adapter.setData(list);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.radioGroup:
                appBarLayout.setExpanded(false, false);
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        showToast("加载更多");
    }
}
