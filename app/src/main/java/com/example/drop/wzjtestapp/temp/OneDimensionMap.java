package com.example.drop.wzjtestapp.temp;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.views.MyHorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 运动的一维地图 用于网约公交车  2019-2-12
 *
 */
public class OneDimensionMap extends Activity {

    @BindView(R.id.recyclePosition) RecyclerView recyclePosition;
    @BindView(R.id.tvDriverMe) TextView tvDriverMe;
    @BindView(R.id.tvDriver1) TextView tvDriver1;
    @BindView(R.id.layoutMap) RelativeLayout layoutMap;
    @BindView(R.id.scrollMap) MyHorizontalScrollView scrollMap;
    @BindView(R.id.btnMoveTo) Button btnMoveTo;
    @BindView(R.id.btnMoveBack) Button btnMoveBack;
    @BindView(R.id.btnScroll) Button btnScroll;
    private static int FORWARD = 1;//向前
    private static int BACKWARD = 2;//向后
    private Context mContext;
    private int driver1Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map);
        ButterKnife.bind(this);

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

    /**
     * 以列表的形式实现
     */
    private void initList() {
        RecyclerView recyclePosition = (RecyclerView) findViewById(R.id.recyclePosition);
        recyclePosition.setVisibility(View.GONE);
        List<MapItem> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(new MapItem(i));
        }
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclePosition.setLayoutManager(manager);
        recyclePosition.setAdapter(new HomeAdapter(R.layout.item_one_dimension_map, list));
    }

    public class MapItem {
        public int id;

        public MapItem(int id) {
            this.id = id;
        }

    }
}
