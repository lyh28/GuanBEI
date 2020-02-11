package com.lyh.guanbei.ui.widget.pattenerLocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lyh.guanbei.R;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class PatternIndicatorView extends View {
    private Paint mPaint;
    private List<Integer> mHitList;     //已经经过的点
    private static final int NOT_SET = -1;
    //可设置的常量
    private int mPointNum;      //总的点数
    private int mCircleColor;   //圆圈外围颜色
    private int mPadding;       //每个点之间的间隔
    private int mCircleR;    //圆圈外围大小
    private int mCircleWidth;   //圆圈外围的粗细
    private int mHitR;      //已经经过的点绘制的半径

    //需要的数据
    private int mSize;  //整个View的大小
    private int mEdgeN; //每个边的个数
    private int mOneSize;   //每个格的大小
    private int mOneR;      //每个格长度的一半
    private static final String BLUE_COLOR="#2196F3";

    public PatternIndicatorView(Context context) {
        super(context);
        init(null);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.PatternIndicatorView);
        init(typedArray);
    }

    private void init(TypedArray typedArray) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHitList = new ArrayList<>();
        mPointNum = 9;
        mEdgeN = (int) Math.sqrt(mPointNum);
        mCircleColor = Color.parseColor(BLUE_COLOR);
        mPadding = NOT_SET;
        mCircleR = NOT_SET;
        mHitR = NOT_SET;
        mCircleWidth = 3;
        if(typedArray!=null){
            mPointNum=typedArray.getInteger(R.styleable.PatternIndicatorView_point_num,9);
            mCircleColor=typedArray.getColor(R.styleable.PatternIndicatorView_circle_color,Color.parseColor(BLUE_COLOR));
            mCircleWidth=typedArray.getInteger(R.styleable.PatternIndicatorView_circle_width,3);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制底面圆圈
        onDrawBase(canvas);
    }

    //绘制底部n个圆
    private void onDrawBase(Canvas canvas) {
        try {
            mPaint.setStrokeWidth(mCircleWidth);
            mPaint.setColor(mCircleColor);
            int[] XY = new int[2];
            for (int i = 0; i < mPointNum; i++) {
                getCenterXYByIndex(i, XY);
                if (mHitList.contains(i)) {
                    mPaint.setStyle(Paint.Style.FILL);
                } else {
                    mPaint.setStyle(Paint.Style.STROKE);
                }
                canvas.drawCircle(XY[0], XY[1], mCircleR, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //根据index得到方格中心点位置
    private void getCenterXYByIndex(int index, int[] XY) throws Exception {
        if (XY.length < 2)
            throw new Exception("需传入长度大于等于2的数组");
        int i, j;    //i 行数  j  列数
        i = index / mEdgeN;
        j = index % mEdgeN;
        XY[0] = mOneSize * j + mOneR;
        XY[1] = mOneSize * i + mOneR;
    }


    public void clean() {
        mHitList.clear();
        invalidate();
    }

    public void updateHitPoint(List<Integer> points) {
        mHitList = points;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        mSize = Math.min(width, height);
        super.onMeasure(MeasureSpec.makeMeasureSpec(mSize,widthMode), MeasureSpec.makeMeasureSpec(mSize,heightMode));
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mOneSize = mSize / mEdgeN;
        mOneR = mOneSize / 2;
        if (mPadding == NOT_SET)
            mPadding = mOneSize / 6;
        if (mCircleR == NOT_SET)
            mCircleR = mOneSize / 3;
        if (mHitR == NOT_SET)
            mHitR = mOneSize / 12;
    }
}
