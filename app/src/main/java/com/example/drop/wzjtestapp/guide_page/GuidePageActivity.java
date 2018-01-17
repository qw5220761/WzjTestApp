package com.example.drop.wzjtestapp.guide_page;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.drop.wzjtestapp.R;
import com.example.drop.wzjtestapp.utils.ImageUtils;
import com.example.drop.wzjtestapp.utils.ToastUtil;

public class GuidePageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private int[] imgRes;
    private ImageView[] views;
    private Button experience;
    private String extraMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);
        initView();
    }
    private void initView(){
        mViewPager = (ViewPager) findViewById(R.id.first_viewpager);
        imgRes = new int[] { R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3, R.mipmap.guide4};
        views = new ImageView[imgRes.length];
        for (int i = 0; i < imgRes.length; i++) {
            ImageView image = new ImageView(this);
            // 避免OOM
            ImageUtils.showImage(GuidePageActivity.this, image, imgRes[i]);
            views[i] = image;
        }
//		container = (LinearLayout) findViewById(R.id.container);
        experience = (Button) findViewById(R.id.immediately_experience);
        mViewPager.setAdapter(new GuidePagerAdapter());
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                for (int i = 0; i < views.length; i++) {
                    if (arg0 == views.length - 1) {
                        views[arg0].setOnClickListener(new goMainListener());
                    } else {
                        views[i].setOnClickListener(null);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }
    private class GuidePagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(views[position]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(views[position]);
            return views[position];
        }
    }
    private class goMainListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ToastUtil.showToast("显示首页");
        }

    }
}
