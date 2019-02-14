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
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
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
    private List<Points> pointsList;
    private int myPosition;
    private int marginStart = 80;//当前司机距离左边得距离

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
                pointsList.get(myPosition).isMyPosition = false;
                if (myPosition < pointsList.size() - 6) {
                    myPosition = myPosition + 5;
                }
                pointsList.get(myPosition).isMyPosition = true;
                initData();
            }
        });
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
            //装入乘客信息
            if (i == 2 || ((i - 2) % 20) == 0 || i == 97) {
                List<Passenger> passengerList = new ArrayList<>();
                Passenger passenger = new Passenger();
                passenger.passengerId = i;
                passengerList.add(passenger);
                point.passengerList = passengerList;
            }
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

    public void readLine(Context context, String fileName) {
        pointsList = new ArrayList<>();
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
            String lat = split[0];
            String lng = split[1];
            Points p = new Points();
            p.lat = Double.parseDouble(lat);
            p.lng = Double.parseDouble(lng);

            pointsList.add(p);
        }
        return "";
    }

    private void initGaoDeMap(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        gaoDeMap.onCreate(savedInstanceState);
        AMap aMap = gaoDeMap.getMap();

        float distance = AMapUtils.calculateLineDistance(new LatLng(43.886993, 126.526241), new LatLng(43.861254, 126.633358));
        ToastUtil.showToast("距离是：" + distance);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(43.886993, 126.526241), 12f));

        AMapLocationClient mlocationClient;
        AMapLocationClientOption mLocationOption = null;
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                        amapLocation.getLatitude();//获取纬度
                        amapLocation.getLongitude();//获取经度
                        amapLocation.getAccuracy();//获取精度信息
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date(amapLocation.getTime());
                        df.format(date);//定位时间
                        LogUtil.d(df.toString()+":"+amapLocation.getLatitude());
                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        });
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
    }
}
