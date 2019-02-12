package com.example.drop.wzjtestapp.temp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.views.MyHorizontalScrollView;
import com.example.drop.wzjtestapp.views.temp.PointView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

/**
 * 静态刷新的一维地图 用于网约公交车  2019-2-12
 */
public class OneDimensionMap_Static extends Activity {
    @BindView(R.id.tvDriver1) TextView tvDriver1;
    @BindView(R.id.layoutMap) RelativeLayout layoutMap;
    @BindView(R.id.scrollMap) MyHorizontalScrollView scrollMap;
    @BindView(R.id.btnMoveTo) Button btnMoveTo;
    @BindView(R.id.btnMoveBack) Button btnMoveBack;
    @BindView(R.id.btnScroll) Button btnScroll;
    @BindView(R.id.layoutPoints) LinearLayout layoutPoints;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map_static);
        ButterKnife.bind(this);
        tvDriver1.setVisibility(View.GONE);
        for(int i=0;i<100;i++){
            PointView point = new PointView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(10),dip2px(10));
            params.gravity = Gravity.CENTER;
            point.setLayoutParams(params);
            point.setBackgroundColor(Color.YELLOW);
            if(i==2||((i-2)%20)==0||i==97){
                point.setViewType(2);
                TextView tvStationName = new TextView(this);
                tvStationName.setText("站点名称"+i);
                tvStationName.setEms(1);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMarginStart(dip2px(10*i));
                tvStationName.setLayoutParams(param);
                layoutMap.addView(tvStationName);
            }
            layoutPoints.addView(point);
        }
        for(int i=0;i<4;i++){
            PointView point = new PointView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip2px(20),dip2px(20));
            params.setMarginStart(40*i);
            params.topMargin = 20;
            point.setLayoutParams(params);
            point.setBackground(getResources().getDrawable(R.mipmap.car));
            point.setViewType(1);
            layoutMap.addView(point);
        }
        for(int i=0;i<10;i++){
            PointView point = new PointView(this);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dip2px(20),dip2px(20));
            params.setMarginStart(100+40*i);
            params.bottomMargin = 20;
            params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            point.setLayoutParams(params);
            point.setBackground(getResources().getDrawable(R.mipmap.people));
            point.setViewType(1);
            layoutMap.addView(point);
        }



    }
    public class Points{
        public int latLng;
        public String stationName;
    }
    public int dip2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
