package com.example.drop.wzjtestapp.temp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
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
import com.example.drop.wzjtestapp.utils.ArrayUtil;
import com.example.drop.wzjtestapp.utils.WzjUtils;
import com.example.drop.wzjtestapp.views.MyHorizontalScrollView;
import com.example.drop.wzjtestapp.views.temp.PointView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 静态刷新的一维地图 用于网约公交车  2019-2-12
 */
public class OneDimensionMap_Static extends Activity {
    @BindView(R.id.layoutMap) RelativeLayout layoutMap;
    @BindView(R.id.scrollMap) MyHorizontalScrollView scrollMap;
    @BindView(R.id.btnMoveTo) Button btnMoveTo;
    @BindView(R.id.btnMoveBack) Button btnMoveBack;
    @BindView(R.id.btnScroll) Button btnScroll;
    private List<Points> pointsList ;
    private int myPosition;
    private int marginStart = 80;//当前司机距离左边得距离

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map_static);
        ButterKnife.bind(this);
        createTestData();
        initData();
        //乘客前进
        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = -1;
                layoutMap.removeAllViews();

                //造一个乘客移动得假数据
                for(int i = 0;i<pointsList.size();i++){
                    if(!ArrayUtil.isEmpty(pointsList.get(i).passengerList)){
                        if(i<90&&currentIndex!=i){
                            pointsList.get(i).passengerList = null;
                            currentIndex = i+5;
                            Passenger passenger = new Passenger();
                            passenger.passengerId = i;
                            List<Passenger> passengerList = new ArrayList<>();
                            passengerList.add(passenger);
                            pointsList.get(currentIndex).passengerList=passengerList;
                        }
                    }
                }
                initData();
            }
        });
        //其它司机前进
        btnMoveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMap.removeAllViews();
            }
        });
        //当前司机前进
        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMap.removeAllViews();
                pointsList.get(myPosition).isMyPosition = false;
                if(myPosition<95){
                    myPosition = myPosition+5;
                }
                pointsList.get(myPosition).isMyPosition = true;
                initData();
            }
        });
    }

    private void initData(){
        LinearLayout layoutPoints = new LinearLayout(this);
        RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pointParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutPoints.setLayoutParams(pointParams);
        layoutPoints.setOrientation(LinearLayout.HORIZONTAL);
        layoutPoints.setBackgroundColor(Color.RED);
        layoutMap.addView(layoutPoints);

        //加载头部
        PointView pointHead = new PointView(this);
        LinearLayout.LayoutParams paramsHead = new LinearLayout.LayoutParams(dip2px(80),dip2px(10));
        paramsHead.gravity = Gravity.CENTER;
        pointHead.setLayoutParams(paramsHead);
        pointHead.setBackgroundColor(Color.YELLOW);
        pointHead.setViewType(-1);
        layoutPoints.addView(pointHead);

        for(int i=0;i<pointsList.size();i++){
            Points pointData= pointsList.get(i);
            PointView point = new PointView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(10),dip2px(10));
            params.gravity = Gravity.CENTER;
            point.setLayoutParams(params);
            point.setBackgroundColor(Color.YELLOW);
            layoutPoints.addView(point);

            //加载站点信息
            if(!TextUtils.isEmpty(pointData.stationName)){
                point.setViewType(2);
                TextView tvStationName = new TextView(this);
                tvStationName.setText("站点名称"+i);
                tvStationName.setEms(1);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMarginStart(dip2px(10*i+marginStart));
                tvStationName.setLayoutParams(param);
                layoutMap.addView(tvStationName);
            }

            //加载其他司机信息
            if(!ArrayUtil.isEmpty(pointData.driverCarList)){
                PointView pointDriver = new PointView(this);
                RelativeLayout.LayoutParams paramsDriver = new RelativeLayout.LayoutParams(dip2px(20),dip2px(20));
                paramsDriver.setMarginStart(dip2px(10*i+marginStart));
                paramsDriver.topMargin = 20;
                pointDriver.setLayoutParams(paramsDriver);
                pointDriver.setBackground(getResources().getDrawable(R.mipmap.car));
                pointDriver.setViewType(1);
                layoutMap.addView(pointDriver);
            }

            //加载乘客信息
            if(!ArrayUtil.isEmpty(pointData.passengerList)){
                PointView pointPassenger = new PointView(this);
                RelativeLayout.LayoutParams paramsPassenger = new RelativeLayout.LayoutParams(dip2px(20),dip2px(20));
                paramsPassenger.setMarginStart(dip2px(10*i+marginStart));
                paramsPassenger.bottomMargin = 20;
                paramsPassenger.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                pointPassenger.setLayoutParams(paramsPassenger);
                pointPassenger.setBackground(getResources().getDrawable(R.mipmap.people));
                pointPassenger.setViewType(1);
                layoutMap.addView(pointPassenger);
            }

            //加载当前司机信息
            if(pointData.isMyPosition){
                myPosition = i;
                point.setViewType(3);

            }
        }

        //加载右边距得距离
        PointView pointFoot = new PointView(this);
        LinearLayout.LayoutParams paramsFoot = new LinearLayout.LayoutParams(dip2px(WzjUtils.getScreenWidth(this)-80),dip2px(10));
        paramsFoot.gravity = Gravity.CENTER;
        pointFoot.setLayoutParams(paramsFoot);
        pointFoot.setBackgroundColor(Color.YELLOW);
        pointFoot.setViewType(-1);
        layoutPoints.addView(pointFoot);

        scrollMap.scrollTo(dip2px(myPosition*10),0);
    }
    private void createTestData() {
        pointsList = new ArrayList<>();
        for(int i = 0;i<100;i++){
            Points point = new Points();
            //装入站点信息
            if(i==2||((i-2)%20)==0||i==97){
                point.stationName = "站点"+i;
            }
            //装入其他司机信息
            if(i == 10||i==40||i==60){
                List<DriverCar> driverCarList = new ArrayList<>();
                DriverCar driverCar = new DriverCar();
                driverCar.driverId = i;
                driverCar.driverName = "0"+i;
                driverCarList.add(driverCar);
                point.driverCarList = driverCarList;
            }
            //装入乘客信息
            if(i==2||((i-2)%20)==0||i==97){
                List<Passenger> passengerList = new ArrayList<>();
                Passenger passenger = new Passenger();
                passenger.passengerId = i;
                passengerList.add(passenger);
                point.passengerList = passengerList;
            }
            //装入当前司机信息
            if(i == 8){
                point.isMyPosition = true;
            }
            pointsList.add(point);
        }
    }

    public class Points{
        public int index;
        public int latLng;
        public String stationName;
        public boolean isMyPosition = false;
        public List<DriverCar> driverCarList;
        public List<Passenger> passengerList;
    }
    public class DriverCar{
        public int driverId;
        public int latLng;
        public String driverName;

    }
    public class Passenger{
        public int passengerId;
        public int latLng;
    }
    public int dip2px( float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
