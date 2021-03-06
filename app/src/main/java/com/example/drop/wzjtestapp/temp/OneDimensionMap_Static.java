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

import com.alibaba.fastjson.JSON;
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
    @BindView(R.id.scrollMap) BusMapView scrollMap;
    @BindView(R.id.btnSetPassenger) Button btnSetPassenger;
    @BindView(R.id.btnSetDriver) Button btnSetDriver;
    @BindView(R.id.btnMyPosition) Button btnMyPosition;
    @BindView(R.id.gaoDeMap) MapView gaoDeMap;
    @BindView(R.id.tvLatLng) TextView tvLatLng;
    @BindView(R.id.btnPx) Button btnPx;

    private int myPosition = 0;
    private int marginStart = 80;//当前司机距离左边得距离
    LatLng mCenterLatLonPoint = null;//中心点的位置
    private AMap aMap;
    private Marker orderMarker;
    List<BusMapView.DriverCar> driverCarList;
    List<BusMapView.Passenger> passengerList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map_static);
        ButterKnife.bind(this);
        scrollMap.initMap(createTestData());
        initGaoDeMap(savedInstanceState);
        //设置乘客
        btnSetPassenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passengerList = new ArrayList<>();
                BusMapView.Passenger passenger = new BusMapView.Passenger();
                passenger.latLng = mCenterLatLonPoint;
                passengerList.add(passenger);
                scrollMap.setPassengerPosition(passengerList);
            }
        });
        //设置司机
        btnSetDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                driverCarList = new ArrayList<>();
                BusMapView.DriverCar driverCar = new BusMapView.DriverCar();
                driverCar.latLng = mCenterLatLonPoint;
                driverCar.driverName = "001";
                driverCarList.add(driverCar);
                scrollMap.setDriverPosition(driverCarList);
            }
        });
        //设置当前位置
        btnMyPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scrollMap.setMyPosition(mCenterLatLonPoint);

            }
        });
        //拼线
        btnPx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order();
            }
        });
    }

    //预约
    private void order() {
        if(mCenterLatLonPoint!=null) {
            int index = scrollMap.latLngToPxPoint(mCenterLatLonPoint);
            if(index == BusMapView.TRANSLATE_FAIL){
                ToastUtil.showToast("一维地图映射失败");
                return;
            }
            //赋值
            BusMapView.Passenger passenger = new BusMapView.Passenger();
            passenger.passengerId = index;
            if (ArrayUtil.isEmpty(pointsList.get(index).passengerList)) {
                pointsList.get(index).passengerList = new ArrayList<>();
            }
            pointsList.get(index).passengerList.add(passenger);
            if(orderMarker !=null){
                orderMarker.remove();
            }
            orderMarker = aMap.addMarker(new MarkerOptions().position(new LatLng(pointsList.get(index).lat,pointsList.get(index).lng)).title("映射位置").snippet("DefaultMarker"));

        }
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

    private List<BusMapView.Points> pointsList;
    private String createTestData() {

        //读取轨迹文件
        readLine(this, "公交站点.txt");
        for (int i = 0; i < pointsList.size(); i++) {

            BusMapView.Points point = pointsList.get(i);
            //装入站点信息
            if (i == 2 || ((i - 2) % 20) == 0 || i == 97) {
                point.stationName = "站点" + i;
            }
        }
        return JSON.toJSONString(pointsList);
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
            BusMapView.Points p = new BusMapView.Points();
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
