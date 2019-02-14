package com.example.drop.wzjtestapp.temp;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.utils.ReadAndWriteTextUtil;
import com.example.drop.wzjtestapp.utils.WzjUtils;
import com.example.drop.wzjtestapp.views.MyHorizontalScrollView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

/**
 * 运动的一维地图 用于网约公交车  2019-2-12
 * 读取文件  2019-2-14
 *
 */
public class OneDimensionMap extends Activity {

    @BindView(R.id.tvDriverMe) TextView tvDriverMe;
    @BindView(R.id.tvDriver1) TextView tvDriver1;
    @BindView(R.id.layoutMap) RelativeLayout layoutMap;
    @BindView(R.id.scrollMap) MyHorizontalScrollView scrollMap;
    @BindView(R.id.btnMoveTo) Button btnMoveTo;
    @BindView(R.id.btnMoveBack) Button btnMoveBack;
    @BindView(R.id.btnScroll) Button btnScroll;
    @BindView(R.id.btnReadText) Button btnReadText;
    @BindView(R.id.btnWriteText) Button btnWriteText;
    @BindView(R.id.tvReadText) TextView tvReadText;
    private static int FORWARD = 1;//向前
    private static int BACKWARD = 2;//向后
    private Context mContext;
    private int driver1Position = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map);
        ButterKnife.bind(this);

        //申请读写权限
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            requestPermissions(PERMISSIONS_STORAGE,110);
        }

        mContext = this;
        scrollMap.setScrollContainer(scrollMap);

        createMap();
        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveView(tvDriver1, 100, FORWARD);
            }
        });
        btnMoveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveView(tvDriver1, 100, BACKWARD);
            }
        });

        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                scrollMap.fling(1);
//                scrollMap.smoothScrollTo(300,0);
                scrollMap.scrollToPosition(300, 0);
            }
        });
        //读取文件
        btnReadText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ReadAndWriteTextUtil.readLine(OneDimensionMap.this,"公交站点.txt");
                tvReadText.setText(s);
            }
        });
        //读取文件
        btnWriteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStringParam(tvReadText.getText().toString());


                ReadAndWriteTextUtil util = new ReadAndWriteTextUtil();
//                String[] strs = new String[]{ "当前时间：" +WzjUtils.getCurrentTime()+"\n" +s};
                //写文件遇到java.io.FileNotFoundException: /storage/emulated/0/txt: open failed: EACCES (Permission denied)问题
//                util.write(strs,OneDimensionMap.this);
            }
        });


    }

    /**
     * 读取文本中的字符串
     * @param s
     * @return
     */
    private String  getStringParam(String s){

        //找到__Location:字符串和#结束字符串的位置
        int start = s.indexOf("__Location:");
        int end = s.indexOf("# ,# ,__DifLocation");
        //-1表示没有这个字符串
        if(start!=-1&&end!=-1){
            //截取需要的字符串
            String str = s.substring(start+"__Location:".length(),end);

            //逗号分割字符串
            String[] split = str.split(",");
            String lat = split[0];
            String lng = split[1];
            tvReadText.setText(String.format("纬度：%s  经度：%s",lat,lng));
        }
        return "";
    }

    private void createMap() {
        RelativeLayout layoutMap = findViewById(R.id.layoutMap);
        layoutMap.setLayoutParams(new FrameLayout.LayoutParams(dip2px(this, 1000), dip2px(this, 100)));
    }

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @param view      要移动的view
     * @param distance  移动的距离
     * @param direction 移动的方向 FORWARD向前 BACKWARD向后
     */
    public void moveView(final View view, final int distance, final int direction) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(view.getLayoutParams());
        layoutParams.setMarginStart(dip2px(this, driver1Position));
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        view.setLayoutParams(layoutParams);
        ObjectAnimator animator;
        if (direction == FORWARD) {
            animator = ObjectAnimator.ofFloat(view, "translationX", 0, dip2px(this, distance));
        } else {
            animator = ObjectAnimator.ofFloat(view, "translationX", 0, -dip2px(this, distance));
        }

        animator.setDuration(2000);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (direction == FORWARD) {
                    driver1Position = distance + driver1Position;
                } else {
                    driver1Position = driver1Position - distance;
                }


            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public class HomeAdapter extends BaseQuickAdapter<MapItem, BaseViewHolder> {
        public HomeAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MapItem item) {
            helper.setText(R.id.text, item.id + "");
            Log.e("aaa", "bbb");
        }
    }


    public class MapItem {
        public int id;

        public MapItem(int id) {
            this.id = id;
        }

    }
}
