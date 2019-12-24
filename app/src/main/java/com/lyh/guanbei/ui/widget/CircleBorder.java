package com.lyh.guanbei.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.lyh.guanbei.R;

import androidx.annotation.ColorInt;

public class CircleBorder extends View {
    private Paint paint;
    private int mBorderWidth;
    private int r;
    private int centerX;
    private int centerY;
    private int status;     //状态  0上半圆   1下半圆    2全圆
    private int mColor;   //画笔颜色
    public CircleBorder(Context context) {
        super(context);
        init(null);
    }

    public CircleBorder(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.CircleBorder);
        init(typedArray);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        r=Math.min(w,h)/2-mBorderWidth;
        centerX=w/2;
        centerY=h/2;
    }

    private void init(TypedArray typedArray){
        mColor=Color.BLACK;
        status=2;
        mBorderWidth=10;
        if(typedArray!=null){
            mColor=typedArray.getColor(R.styleable.CircleBorder_color,Color.BLACK);
            status=typedArray.getInt(R.styleable.CircleBorder_circle_type,2);
            mBorderWidth=typedArray.getInteger(R.styleable.CircleBorder_border_width,5);
        }
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        RectF rectF=new RectF(centerX-r,centerY-r,centerX+r,centerY+r);
        paint.setStrokeWidth(mBorderWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(mColor);
        if(status==0){
            canvas.drawArc(rectF,180,360,false,paint);
        }else if(status==1){
            canvas.drawArc(rectF,0,180,false,paint);
        }else if(status==2){
            canvas.drawCircle(centerX,centerY,r,paint);
        }
    }
    public void setColor(@ColorInt int color){
        mColor=color;
        invalidate();
    }
    public void setBorderWidth(int width){
        mBorderWidth=width;
        invalidate();
    }
}
