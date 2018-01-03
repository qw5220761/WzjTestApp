package com.example.drop.wzjtestapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.database.bean.TestData;
import com.example.drop.wzjtestapp.utils.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang on 2016/10/26.
 */
public class OrdersListAdapter extends CommonWrapper<OrdersListAdapter.OrdersListHolder>{

    private Context mContext;
    private List<TestData> datas = new ArrayList<TestData>();
    private Fragment fragment;

    public OrdersListAdapter(Context context, List<TestData> datas){
        super();
        this.mContext = context;
        this.datas = datas;
    }

    public OrdersListAdapter(Fragment fragment, List<TestData> datas){
        super();
        this.fragment = fragment;
        this.mContext = fragment.getActivity();
        this.datas = datas;
    }


    public void setData(List<TestData> datas){
        if(datas==null){
            datas = new ArrayList<TestData>();
        }
        this.datas = datas;
        notifyWrapperDataSetChanged();
    }

    public void addAll(List<TestData> datas){
        if(datas==null){
            datas = new ArrayList<TestData>();
        }
        this.datas.addAll(datas);
        notifyWrapperDataSetChanged();
    }

    public boolean isEmpty(){
        return ArrayUtil.isEmpty(datas);
    }

    @Override
    public OrdersListHolder onCreateViewHolder(ViewGroup parent, int i) {
        OrdersListHolder vh = new OrdersListHolder(LayoutInflater.from(mContext).inflate(R.layout.item_list_product, parent, false));
        return vh;
    }
    @Override
    public void onBindViewHolder(OrdersListHolder viewHolder, int position) {
        final TestData bean = datas.get(position);
        if(bean == null) {
            return;
        }
        viewHolder.tvListName.setText(bean.getName());
        viewHolder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, MainActivity.class);
//                if(fragment!=null){
//                    fragment.startActivityForResult(intent, REQUEST_CODE_ORDERS_LIST);
//                }else if(mContext!=null){
//                    ((Activity)mContext).startActivityForResult(intent, REQUEST_CODE_ORDERS_LIST);
//                }
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class OrdersListHolder extends RecyclerView.ViewHolder{

        public View item;
        public TextView tvListName;


        public OrdersListHolder(View itemView) {
            super(itemView);
            this.item = itemView;
            tvListName = (TextView) itemView.findViewById(R.id.tvListName);
        }
    }

    private OnItemClickListener onItemClickListener;
    public interface OnItemClickListener{
        void onItemClick();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

}
