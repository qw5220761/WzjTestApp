package com.example.drop.wzjtestapp.temp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.drop.wzjtestapp.R;

import java.util.ArrayList;
import java.util.List;

public class OneDimensionMap extends Activity {

    private Context mContext;
    private float driver1Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_dimension_map);

        mContext = this;
        final TextView tvDriver1 = findViewById(R.id.tvDriver1);
        TextView tvDriverMe = findViewById(R.id.tvDriverMe);
        ScrollView scrollMap = findViewById(R.id.scrollMap);
        Button btnMoveTo = findViewById(R.id.btnMoveTo);
        Button btnMoveBack = findViewById(R.id.btnMoveBack);

        createMap();
        btnMoveTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToViewRight(tvDriver1, 0, 100);
            }
        });
        btnMoveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToViewRight(tvDriver1, 0, -100);
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
     * @return
     */
    public void moveToViewRight(final View view, final float prePosition, final float postPosition) {
        TranslateAnimation mAction = new TranslateAnimation(dip2px(this, prePosition), dip2px(this, postPosition), 0, 0);
        mAction.setDuration(1000);
        mAction.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(view.getLayoutParams());
                driver1Position = postPosition + driver1Position;
                layoutParams.setMarginStart(dip2px(mContext, driver1Position));
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
                view.setLayoutParams(layoutParams);

            }
        });

        view.startAnimation(mAction);
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
