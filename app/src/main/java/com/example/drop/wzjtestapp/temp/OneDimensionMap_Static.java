package com.example.drop.wzjtestapp.temp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.utils.ArrayUtil;
import com.example.drop.wzjtestapp.utils.LogUtil;
import com.example.drop.wzjtestapp.utils.ToastUtil;
import com.example.drop.wzjtestapp.utils.WzjUtils;
import com.example.drop.wzjtestapp.views.MyHorizontalScrollView;
import com.example.drop.wzjtestapp.views.temp.PointView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @BindView(R.id.gaoDeMap) MapView gaoDeMap;
    @BindView(R.id.tvLatLng) TextView tvLatLng;
    @BindView(R.id.btnPx) Button btnPx;
    private List<Points> pointsList;
    private int myPosition;
    private int marginStart = 80;//当前司机距离左边得距离
    LatLng mCenterLatLonPoint = null;//中心点的位置
    private AMap aMap;
    private Marker orderMarker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map_static);
        ButterKnife.bind(this);
        createTestData();
        initData();
        initGaoDeMap(savedInstanceState);
        //乘客前进
        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentIndex = -1;
                layoutMap.removeAllViews();

                //造一个乘客移动得假数据
                for (int i = 0; i < pointsList.size(); i++) {
                    if (!ArrayUtil.isEmpty(pointsList.get(i).passengerList)) {
                        if (i < pointsList.size() - 10 && currentIndex != i) {
                            pointsList.get(i).passengerList = null;
                            currentIndex = i + 5;
                            Passenger passenger = new Passenger();
                            passenger.passengerId = i;
                            List<Passenger> passengerList = new ArrayList<>();
                            passengerList.add(passenger);
                            pointsList.get(currentIndex).passengerList = passengerList;
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

                if(myPosition<pointsList.size()-5){
                    setMyPosition(myPosition+5);
                }

                initData();
            }
        });
        //拼线
        btnPx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutMap.removeAllViews();
                clearDriverAndPassengerData();
                order();
            }
        });
    }

    //设置当前司机的位置
    private void setMyPosition(int index){
        pointsList.get(myPosition).isMyPosition = false;
        myPosition = index;
        pointsList.get(myPosition).isMyPosition = true;
    }
    //预约
    private void order() {
        List<Points> comparePointsList = new ArrayList<>();
        if(mCenterLatLonPoint!=null){

            for(int i=0;i<pointsList.size();i++){
                Points points = pointsList.get(i);
                float distance = AMapUtils.calculateLineDistance(new LatLng(points.lat,points.lng),mCenterLatLonPoint);
                //小于500米的留下
                if(distance<500){
                    points.distance = distance;
                    comparePointsList.add(points);
                }
            }

            if(comparePointsList.size()>2){
                //冒泡排序比较
                for(int i=0;i<comparePointsList.size();i++){

                    for(int j=0;j<comparePointsList.size()-1;j++){
                        float temp1 = comparePointsList.get(j).distance;
                        float temp2 = comparePointsList.get(j+1).distance;
                        if(temp1>temp2){

                            Points tempPoint = comparePointsList.get(j);
                            comparePointsList.set(j,comparePointsList.get(j+1)) ;
                            comparePointsList.set(j+1,tempPoint) ;
                        }
                    }

                }
            }else {
                ToastUtil.showToast("距离太远，无法预约");
                return;
            }

            //拿到映射到一维地图的点index
            Points thePoint = comparePointsList.get(0);
            //赋值
            Passenger passenger = new Passenger();
            passenger.passengerId = (int)comparePointsList.get(0).distance;
            if(ArrayUtil.isEmpty(pointsList.get(thePoint.index).passengerList)){
                pointsList.get(thePoint.index).passengerList = new ArrayList<>();
            }
            pointsList.get(thePoint.index).passengerList.add(passenger);

            //显示乘客的映射位置
            if(orderMarker!=null){
                orderMarker.remove();
            }
            orderMarker = aMap.addMarker(new MarkerOptions().position(new LatLng(thePoint.lat,thePoint.lng)).title("映射的点").snippet("DefaultMarker"));

            setMyPosition(thePoint.index);
        }

        notifySyncData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        gaoDeMap.onResume();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        gaoDeMap.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gaoDeMap.onDestroy();
    }

    private void initData() {
        LinearLayout layoutPoints = new LinearLayout(this);
        RelativeLayout.LayoutParams pointParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pointParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutPoints.setLayoutParams(pointParams);
        layoutPoints.setOrientation(LinearLayout.HORIZONTAL);
        layoutPoints.setBackgroundColor(Color.RED);
        layoutMap.addView(layoutPoints);

        //加载头部
        PointView pointHead = new PointView(this);
        LinearLayout.LayoutParams paramsHead = new LinearLayout.LayoutParams(dip2px(80), dip2px(10));
        paramsHead.gravity = Gravity.CENTER;
        pointHead.setLayoutParams(paramsHead);
        pointHead.setBackgroundColor(Color.YELLOW);
        pointHead.setViewType(-1);
        layoutPoints.addView(pointHead);

        for (int i = 0; i < pointsList.size(); i++) {
            Points pointData = pointsList.get(i);
            PointView point = new PointView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dip2px(10), dip2px(10));
            params.gravity = Gravity.CENTER;
            point.setLayoutParams(params);
            point.setBackgroundColor(Color.YELLOW);
            layoutPoints.addView(point);

            //加载站点信息
            if (!TextUtils.isEmpty(pointData.stationName)) {
                point.setViewType(2);
                TextView tvStationName = new TextView(this);
                tvStationName.setText("站点名称" + i);
                tvStationName.setEms(1);
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                param.setMarginStart(dip2px(10 * i + marginStart));
                tvStationName.setLayoutParams(param);
                layoutMap.addView(tvStationName);
            }

            //加载其他司机信息
            if (!ArrayUtil.isEmpty(pointData.driverCarList)) {
                PointView pointDriver = new PointView(this);
                RelativeLayout.LayoutParams paramsDriver = new RelativeLayout.LayoutParams(dip2px(20), dip2px(20));
                paramsDriver.setMarginStart(dip2px(10 * i + marginStart));
                paramsDriver.topMargin = 20;
                pointDriver.setLayoutParams(paramsDriver);
                pointDriver.setBackground(getResources().getDrawable(R.mipmap.car));
                pointDriver.setViewType(1);
                layoutMap.addView(pointDriver);
            }

            //加载乘客信息
            if (!ArrayUtil.isEmpty(pointData.passengerList)) {
                PointView pointPassenger = new PointView(this);
                RelativeLayout.LayoutParams paramsPassenger = new RelativeLayout.LayoutParams(dip2px(20), dip2px(20));
                paramsPassenger.setMarginStart(dip2px(10 * i + marginStart));
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
        PointView pointFoot = new PointView(this);
        LinearLayout.LayoutParams paramsFoot = new LinearLayout.LayoutParams(dip2px(WzjUtils.getScreenWidth(this) - 80), dip2px(10));
        paramsFoot.gravity = Gravity.CENTER;
        pointFoot.setLayoutParams(paramsFoot);
        pointFoot.setBackgroundColor(Color.YELLOW);
        pointFoot.setViewType(-1);
        layoutPoints.addView(pointFoot);

        scrollMap.post(new Runnable() {
            @Override
            public void run() {
                scrollMap.scrollTo(dip2px(myPosition * 10), 0);
            }
        });
    }

    private void createTestData() {
        //读取轨迹文件
        readLine(this, "公交站点.txt");
        for (int i = 0; i < pointsList.size(); i++) {
            Points point = pointsList.get(i);
            //装入站点信息
            if (i == 2 || ((i - 2) % 20) == 0 || i == 97) {
                point.stationName = "站点" + i;
            }
            //装入其他司机信息
            if (i == 10 || i == 40 || i == 60) {
                List<DriverCar> driverCarList = new ArrayList<>();
                DriverCar driverCar = new DriverCar();
                driverCar.driverId = i;
                driverCar.driverName = "0" + i;
                driverCarList.add(driverCar);
                point.driverCarList = driverCarList;
            }
//            //装入乘客信息
//            if (i == 2 || ((i - 2) % 20) == 0 || i == 97) {
//                List<Passenger> passengerList = new ArrayList<>();
//                Passenger passenger = new Passenger();
//                passenger.passengerId = i;
//                passengerList.add(passenger);
//                point.passengerList = passengerList;
//            }
            //装入当前司机信息
            if (i == 8) {
                point.isMyPosition = true;
            }
        }
    }


    public class Points {
        public int index;
        public double lat;
        public double lng;
        public float distance;
        public String stationName;
        public boolean isMyPosition = false;
        public List<DriverCar> driverCarList;
        public List<Passenger> passengerList;
    }

    public class DriverCar {
        public int driverId;
        public int latLng;
        public String driverName;

    }

    public class Passenger {
        public int passengerId;
        public int latLng;
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private List<LatLng> latLngs;
    public void readLine(Context context, String fileName) {
        pointsList = new ArrayList<>();
        latLngs = new ArrayList<LatLng>();
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(fileName);
        } catch (Exception e1) {
            if (e1 instanceof FileNotFoundException) {
                ToastUtil.showToast("未找到文件");
            }
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                getStringParam(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearDriverAndPassengerData(){
        for (Points p:pointsList){
            p.passengerList = null;
            p.driverCarList = null;
        }
    }
    private void notifySyncData(){

        initData();
    }
    int index = 0;
    /**
     * 读取文本中的字符串
     *
     * @param s
     * @return
     */
    private String getStringParam(String s) {

        //找到__Location:字符串和#结束字符串的位置
        int start = s.indexOf("__Location:");
        int end = s.indexOf("# ,# ,__DifLocation");
        //-1表示没有这个字符串
        if (start != -1 && end != -1) {
            //截取需要的字符串
            String str = s.substring(start + "__Location:".length(), end);

            //逗号分割字符串
            String[] split = str.split(",");
            String lng = split[0];
            String lat = split[1];
            Points p = new Points();
            p.index = index;
            p.lat = Double.parseDouble(lat);
            p.lng = Double.parseDouble(lng);
            latLngs.add(new LatLng(p.lat,p.lng));

            pointsList.add(p);
            index++;
        }
        return "";
    }

    private void initGaoDeMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        gaoDeMap.onCreate(savedInstanceState);
         aMap= gaoDeMap.getMap();

        float distance = AMapUtils.calculateLineDistance(new LatLng(43.886993, 126.526241), new LatLng(43.861254, 126.633358));
        ToastUtil.showToast("距离是：" + distance);


        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

                mCenterLatLonPoint = new LatLng(cameraPosition.target.latitude, cameraPosition.target.longitude);
                tvLatLng.setText(cameraPosition.target.latitude+","+cameraPosition.target.longitude);
            }
        });
        aMap.addPolyline(new PolylineOptions().
                addAll(latLngs).width(10).color(Color.argb(255, 1, 1, 1)));

        // 显示自己的位置自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point));//
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0f);// 设置圆形的边框粗细
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        aMap.setMyLocationEnabled(true);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));

    }
}
