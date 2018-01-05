package com.example.drop.wzjtestapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drop.wzjtestapp.Constant;
import com.example.drop.wzjtestapp.MyApplication;
import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.database.bean.TestData;
import com.example.drop.wzjtestapp.utils.ArrayUtil;
import com.example.drop.wzjtestapp.utils.LogUtil;
import com.example.drop.wzjtestapp.views.calendar.DatePickerActivity;
import com.example.drop.wzjtestapp.views.other.WaveViewActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 2016/10/26.
 */
public class OrdersListAdapter extends CommonWrapper<OrdersListAdapter.OrdersListHolder> {

    private Context mContext;
    private List<TestData> datas = new ArrayList<TestData>();
    private Fragment fragment;

    public OrdersListAdapter(Context context, List<TestData> datas) {
        super();
        this.mContext = context;
        this.datas = datas;
    }

    public OrdersListAdapter(Fragment fragment, List<TestData> datas) {
        super();
        this.fragment = fragment;
        this.mContext = fragment.getActivity();
        this.datas = datas;
    }


    public void setData(List<TestData> datas) {
        if (datas == null) {
            datas = new ArrayList<TestData>();
        }
        this.datas = datas;
        notifyWrapperDataSetChanged();
    }

    public void addAll(List<TestData> datas) {
        if (datas == null) {
            datas = new ArrayList<TestData>();
        }
        this.datas.addAll(datas);
        notifyWrapperDataSetChanged();
    }

    public boolean isEmpty() {
        return ArrayUtil.isEmpty(datas);
    }

    @Override
    public OrdersListHolder onCreateViewHolder(ViewGroup parent, int i) {
        OrdersListHolder vh = new OrdersListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_product, parent, false));
        return vh;
    }

    @Override
    public void onBindViewHolder(OrdersListHolder viewHolder, final int position) {
        final TestData bean = datas.get(position);
        if (bean == null) {
            return;
        }
        viewHolder.ivListPic.setImageURI(Uri.parse(bean.getImage()));
        viewHolder.tvListName.setText(bean.getName());
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bean.getTitle().equals("waveView")){
                    mContext.startActivity(new Intent(mContext,WaveViewActivity.class));
                }
                if(bean.getTitle().equals("DatePicker")){
                    mContext.startActivity(new Intent(mContext,DatePickerActivity.class));
                }
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class OrdersListHolder extends RecyclerView.ViewHolder {

        public View item;
        public TextView tvListName;
        public SimpleDraweeView ivListPic;


        public OrdersListHolder(View itemView) {
            super(itemView);
            this.item = itemView;
            tvListName = (TextView) itemView.findViewById(R.id.tvListName);
            ivListPic = (SimpleDraweeView) itemView.findViewById(R.id.ivListPic);
        }
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
