package com.lyh.guanbei.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.lyh.guanbei.R;


public class WaveBollView extends View {
    private int color;      //波浪颜色
    private int alpha;      //透明度
    private int r;          //球的半径
    private int offset;
    private int maxOffset;      //可最大的偏移量
    private int wavelen;    //一半波浪的长度
    private int waveH;      //波浪起复的高度
    private int percent;         //百分比
    private int circleX;
    private int circleY;

    private Paint paint;
    private Path wavePath;
    private Path circlePath;
    private Path upPath;

    private boolean isfirst=true;

    private final int FREE_COLOR=Color.parseColor("#89E651");
    private final int FILL_COLOR=Color.parseColor("#FA6868");
    public WaveBollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    private void init(){
        percent=0;
        offset=0;

        color=FILL_COLOR;
        alpha=175;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);


        wavePath=new Path();
        circlePath=new Path();
        upPath=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        get(widthMeasureSpec);
        get(heightMeasureSpec);
        int res=Math.min(width,height);
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(res,MeasureSpec.getMode(widthMeasureSpec))
                ,MeasureSpec.makeMeasureSpec(res,MeasureSpec.getMode(heightMeasureSpec)));
    }
    private void get(int M){
//        switch (MeasureSpec.getMode(M)){
//            case MeasureSpec.AT_MOST:
//                Log.d(TAG, "onMeasure: AT_MOST");  break;
//            case MeasureSpec.EXACTLY:
//                Log.d(TAG, "onMeasure: EXACTLY");  break;
//            case MeasureSpec.UNSPECIFIED:
//                Log.d(TAG, "onMeasure: UNSPECIFIED");  break;
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int tmp=Math.min(w,h);
        circleX=w/2;
        circleY=h/2;

        r=tmp/2;
        wavelen=r*3/2;
        maxOffset=2*wavelen-2*r;
        waveH=(int)(wavelen/2*Math.tan(15*Math.PI/180));

        //设置圆的path
        circlePath.addCircle(circleX,circleY,r,Path.Direction.CW);
}

    @Override
    protected void onDraw(Canvas canvas) {
        //保存状态
        canvas.save();
        //裁剪出圆
        canvas.clipPath(circlePath);
        if(percent>=100)
            color=FILL_COLOR;
        else color=FREE_COLOR;
        //绘制波浪
        UpdateWavePath(-20);
        paint.setColor(color);
        paint.setAlpha(alpha-70);
        canvas.drawPath(wavePath,paint);

        UpdateWavePath(-10);
        paint.setColor(color);
        paint.setAlpha(alpha-50);
        canvas.drawPath(wavePath,paint);

        UpdateWavePath(0);
        paint.setColor(color);
        paint.setAlpha(alpha);
        canvas.drawPath(wavePath,paint);


        upPath.op(circlePath,wavePath, Path.Op.DIFFERENCE);
        paint.setColor(Color.WHITE);
        paint.setAlpha(alpha);
        canvas.drawPath(upPath,paint);

        //绘制文字
        paint.setColor(Color.BLACK);
        paint.setTextSize(100f);
        canvas.drawText(percent+"%",circleX-paint.measureText(percent+"%")/2,circleY,paint);

        if(isfirst){
            setAnim();
            isfirst=false;
        }
        //还原状态
        canvas.restore();
    }
    //得到波浪区域的path
    private void UpdateWavePath(int i){
        wavePath.reset();
        //起点
        wavePath.moveTo(circleX-r-maxOffset+offset,getWaveY()+i);
        wavePath.rQuadTo(wavelen/2,waveH,wavelen,0);
        wavePath.rQuadTo(wavelen/2,-waveH,wavelen,0);
        wavePath.lineTo(circleX+r,circleY+r);
        wavePath.lineTo(circleX-r,circleY+r);
        wavePath.close();
    }
    //设置动画
    private void setAnim(){
        ValueAnimator valueAnimator=new ValueAnimator().setDuration(2000);
        PropertyValuesHolder offsetValues=PropertyValuesHolder.ofInt("offset",0,maxOffset,0);
        //PropertyValuesHolder waveYValues=PropertyValuesHolder.ofInt("waveY",waveY,waveY-30,waveY);

        valueAnimator.setValues(offsetValues);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setAnim();
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                offset=(int)animation.getAnimatedValue("offset");
                //waveY=(int)animation.getAnimatedValue("waveY");
                invalidate();
            }
        });
        valueAnimator.start();
    }
    private int getWaveY(){
        return circleY+r-(int)(percent/100f*r*2);
    }
    public void setColor(int color) {
        this.color = color;
    }
    public void setPercent(int percent){
        this.percent=percent;
    }
}
