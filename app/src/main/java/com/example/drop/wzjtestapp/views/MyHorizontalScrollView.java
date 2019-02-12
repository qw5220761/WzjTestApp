package com.example.drop.wzjtestapp.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView  extends HorizontalScrollView {

    private View scrollContainer;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX/20);
    }

    public void setScrollContainer(View view){
        scrollContainer = view;
    }
    public void scrollToPosition(int x,int y) {

        ObjectAnimator xTranslate = ObjectAnimator.ofInt(scrollContainer, "scrollX", x);

        ObjectAnimator yTranslate = ObjectAnimator.ofInt(scrollContainer, "scrollY", y);



        AnimatorSet animators = new AnimatorSet();

        animators.setDuration(1000L);

        animators.playTogether(xTranslate, yTranslate);

        animators.addListener(new Animator.AnimatorListener() {



            @Override

            public void onAnimationStart(Animator arg0) {

                // TODO Auto-generated method stub

            }



            @Override

            public void onAnimationRepeat(Animator arg0) {

                // TODO Auto-generated method stub



            }



            @Override

            public void onAnimationEnd(Animator arg0) {

                // TODO Auto-generated method stub



            }



            @Override

            public void onAnimationCancel(Animator arg0) {

                // TODO Auto-generated method stub



            }

        });

        animators.start();

    }

}
