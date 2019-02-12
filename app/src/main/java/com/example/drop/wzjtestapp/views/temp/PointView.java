package com.example.drop.wzjtestapp.views.temp;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.drop.wzjtestapp.R;

public class PointView extends View {
    private int mViewColor;
    private Paint mTextViewPaint;
    private Rect mTextViewBoubd;
    private int viewType = 0;

    public PointView(Context context) {
//        super(context);
        this(context, null);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
        this(context, attrs, 0);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //3.此处得到attrs.xml里面,我们的自定义的样式属性  Attribute属性的意思,AttributeSet那么就是xml中的属性设置的意思
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PointView, defStyleAttr, 0);
        int atts = array.getIndex(0);
        if (R.styleable.PointView_color == atts) {
            this.setBackgroundColor(array.getInteger(atts, Color.BLACK)); //默认颜色设置为黑色
        }
        array.recycle();//回收

        mTextViewPaint = new Paint();
        //5.设置画布的宽高
        mTextViewBoubd = new Rect();
        //getTextBounds 由调用者返回在边界(分配)的最小矩形包含所有的字符,以隐含原点(0,0)
//        mTextViewPaint.getTextBounds(,0,mTextViewString.length(),mTextViewBoubd);//String text, int start, int end, Rect bounds
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
        this(context, attrs, 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        switch (viewType) {
            case 0://默认的点
                // 设置样式-填充
                mTextViewPaint.setStyle(Paint.Style.FILL);
                //5.绘制画布大小颜色
                mTextViewPaint.setColor(Color.BLUE);
                canvas.translate(dip2px(1), dip2px(1));
                //float left, float top, float right, float bottom, Paint paint
                //参数设置参考本人博客:http://blog.csdn.net/e_inch_photo/article/details/60978088
                canvas.drawRect(new Rect(0, 0, dip2px(8), dip2px(8)), mTextViewPaint);//画布大小,onMeasure中测量大小
                break;
            case 1://显示人和车
                break;
            case 2://站点
                // 设置样式-填充
                mTextViewPaint.setStyle(Paint.Style.FILL);
                //5.绘制画布大小颜色
                mTextViewPaint.setColor(Color.RED);
                canvas.translate(dip2px(1), dip2px(1));
                //float left, float top, float right, float bottom, Paint paint
                //参数设置参考本人博客:http://blog.csdn.net/e_inch_photo/article/details/60978088
                canvas.drawRect(new Rect(0, 0, dip2px(8), dip2px(8)), mTextViewPaint);//画布大小,onMeasure中测量大小

                break;
            default:
                // 设置样式-填充
                mTextViewPaint.setStyle(Paint.Style.FILL);
                //5.绘制画布大小颜色
                mTextViewPaint.setColor(Color.BLUE);
                canvas.translate(dip2px(1), dip2px(1));
                //float left, float top, float right, float bottom, Paint paint
                //参数设置参考本人博客:http://blog.csdn.net/e_inch_photo/article/details/60978088
                canvas.drawRect(new Rect(0, 0, dip2px(8), dip2px(8)), mTextViewPaint);//画布大小,onMeasure中测量大小
                break;
        }
    }

    public void setViewType(int type) {
        viewType = type;
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
