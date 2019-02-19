package com.example.drop.wzjtestapp.temp;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.utils.ArrayUtil;
import com.example.drop.wzjtestapp.utils.ToastUtil;
import com.example.drop.wzjtestapp.utils.WzjUtils;
import com.example.drop.wzjtestapp.views.MyHorizontalScrollView;
import com.example.drop.wzjtestapp.views.temp.PointView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class BusMapView extends HorizontalScrollView {
    public static int TRANSLATE_FAIL = -1;//坐标转换失败
    private int myPosition = -1;//显示地图位置得坐标
    private List<OneDimensionMap_Static.Points> pointsList;
    private RelativeLayout layoutMap;
    private Context mContext;
    private int defaultPosition = 8;//当前位置得index
    private int itemWith = 10;//每个点得距离

    public BusMapView(Context context) {
        this(context, null);
    }

    public BusMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BusMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = inflate(context, R.layout.layout_bus_map, this);
        layoutMap = view.findViewById(R.id.layoutMap);
        mContext = context;
    }

    /**
     * @param pointsList 地图列表
     */
    public void notifySyncData(List<OneDimensionMap_Static.Points> pointsList) {
        this.pointsList = pointsList;
        notifySyncData();
    }

    /**
     * 更新拼线地图
     */
    public void notifySyncData() {

        int marginStart = defaultPosition * itemWith;
        //清空所有得view
        layoutMap.removeAllViews();


        LinearLayout layoutPoints = new LinearLayout(mContext);
        RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutPoints.setLayoutParams(pointParams);
        layoutPoints.setOrientation(LinearLayout.HORIZONTAL);
        layoutPoints.setBackgroundColor(Color.RED);
        layoutMap.addView(layoutPoints);

        //加载头部
        PointView pointHead = new PointView(mContext);
        LinearLayout.LayoutParams paramsHead = new LinearLayout.LayoutParams(dip2px(marginStart), dip2px(10));
        paramsHead.gravity = Gravity.CENTER;
        pointHead.setLayoutParams(paramsHead);
        pointHead.setBackgroundColor(Color.YELLOW);
        pointHead.setViewType(-1);
        layoutPoints.addView(pointHead);

        //加载坐标点信息
        for (int i = 0; i < pointsList.size(); i++) {
            OneDimensionMap_Static.Points pointData = pointsList.get(i);
            PointView point = new PointView(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(10), dip2px(10));
            params.gravity = Gravity.CENTER;
            point.setLayoutParams(params);
            point.setBackgroundColor(Color.YELLOW);
            layoutPoints.addView(point);

            //加载站点信息
            if (!TextUtils.isEmpty(pointData.stationName)) {
                point.setViewType(2);
                TextView tvStationName = new TextView(mContext);
                tvStationName.setText("站点名称" + i);
                tvStationName.setEms(1);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMarginStart(dip2px(itemWith * i + marginStart));
                tvStationName.setLayoutParams(param);
                layoutMap.addView(tvStationName);
            }

            //加载其他司机信息
            if (!ArrayUtil.isEmpty(pointData.driverCarList)) {
                PointView pointDriver = new PointView(mContext);
                RelativeLayout.LayoutParams paramsDriver = new RelativeLayout.LayoutParams(dip2px(20), dip2px(20));
                paramsDriver.setMarginStart(dip2px(itemWith * i + marginStart));
                paramsDriver.topMargin = 20;
                pointDriver.setLayoutParams(paramsDriver);
                pointDriver.setBackground(getResources().getDrawable(R.mipmap.car));
                pointDriver.setViewType(1);
                layoutMap.addView(pointDriver);
            }

            //加载乘客信息
            if (!ArrayUtil.isEmpty(pointData.passengerList)) {
                PointView pointPassenger = new PointView(mContext);
                RelativeLayout.LayoutParams paramsPassenger = new RelativeLayout.LayoutParams(dip2px(20), dip2px(20));
                paramsPassenger.setMarginStart(dip2px(itemWith * i + marginStart));
                paramsPassenger.bottomMargin = 20;
                paramsPassenger.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                pointPassenger.setLayoutParams(paramsPassenger);
                pointPassenger.setBackground(getResources().getDrawable(R.mipmap.people));
                pointPassenger.setViewType(1);
                layoutMap.addView(pointPassenger);
            }

            //加载当前司机信息
            if (pointData.isMyPosition) {
                myPosition = i;
                point.setViewType(3);

            }
        }

        //加载右边距得距离
        PointView pointFoot = new PointView(mContext);
        LinearLayout.LayoutParams paramsFoot = new LinearLayout.LayoutParams(dip2px(WzjUtils.getScreenWidth(mContext) - 80), dip2px(10));
        paramsFoot.gravity = Gravity.CENTER;
        pointFoot.setLayoutParams(paramsFoot);
        pointFoot.setBackgroundColor(Color.YELLOW);
        pointFoot.setViewType(-1);
        layoutPoints.addView(pointFoot);

        //移动到我得位置
        this.post(new Runnable() {
            @Override
            public void run() {
                scrollTo(dip2px(myPosition * itemWith), 0);
            }
        });
    }

    /**
     * 地图坐标转换成一维坐标
     *
     * @return 一维坐标对应得index   TRANSLATE_FAIL(-1)表示转换失败
     */
    public int latLngToPxPoint(LatLng latLng) {
        int index = -1;//映射得点
        List<OneDimensionMap_Static.Points> comparePointsList = new ArrayList<>();
        if (latLng != null) {

            for (int i = 0; i < pointsList.size(); i++) {
                OneDimensionMap_Static.Points points = pointsList.get(i);
                float distance = AMapUtils.calculateLineDistance(new LatLng(points.lat, points.lng), latLng);
                //小于500米的留下
                if (distance < 500) {
                    points.distance = distance;
                    comparePointsList.add(points);
                }
            }

            if (comparePointsList.size() > 2) {
                //冒泡排序比较
                for (int i = 0; i < comparePointsList.size(); i++) {

                    for (int j = 0; j < comparePointsList.size() - 1; j++) {
                        float temp1 = comparePointsList.get(j).distance;
                        float temp2 = comparePointsList.get(j + 1).distance;
                        if (temp1 > temp2) {

                            OneDimensionMap_Static.Points tempPoint = comparePointsList.get(j);
                            comparePointsList.set(j, comparePointsList.get(j + 1));
                            comparePointsList.set(j + 1, tempPoint);
                        }
                    }
                }
                index = comparePointsList.get(0).index;
            }else if(comparePointsList.size() == 1){
                index = comparePointsList.get(0).index;
            } else {
                return -1;
            }

        }
        return index;

    }


    /**
     * 设置显示得位置
     *
     * @param latLng
     */
    public void setMyPosition(LatLng latLng) {
        int index = latLngToPxPoint(latLng);
        if (myPosition != -1) {
            pointsList.get(myPosition).isMyPosition = false;
        }
        myPosition = index;
        pointsList.get(myPosition).isMyPosition = true;
        notifySyncData();
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
