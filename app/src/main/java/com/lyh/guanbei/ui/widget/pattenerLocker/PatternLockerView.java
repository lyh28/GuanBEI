package com.lyh.guanbei.ui.widget.pattenerLocker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lyh.guanbei.R;
import com.lyh.guanbei.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class PatternLockerView extends View {
    private PatternLockerStatusListener mListener;
    private Paint mPaint;
    private List<Integer> mHitList;     //已经经过的点
    private static final int NOT_SET = -1;
    //可设置的常量
    private int mPointNum;      //总的点数
    private int mLineColor;     //线段颜色
    private int mHitColor;      //触摸圆的颜色
    private int mCircleColor;   //圆圈外围颜色
    private int mErrorColor;    //错误时的颜色
    private int mPadding;       //每个点之间的间隔
    private int mCircleR;    //圆圈外围大小
    private int mCircleWidth;   //圆圈外围的粗细
    private int mHitR;      //已经经过的点绘制的半径
    private int mLineWidth;      //线段大小
    private boolean mAutoClean;      //自动清除绘制图案
    private int mCleanDuration;      //自动清除的延迟时长 单位ms

    //需要的数
    enum status {
        FREE,
        ING,
        COMPLETE,
        ERROR,
    }

    private status mStatus;     //状态
    private int mSize;  //整个View的大小
    private int mEdgeN; //每个边的个数
    private int mOneSize;   //每个格的大小
    private int mOneR;      //每个格长度的一半
    private int mCurrentIndex;  //最近触摸的index
    private int mTouchX;        //当前触摸点X
    private int mTouchY;        //当前触摸点Y
    private static final String BLUE_COLOR="#2196F3";
    private static final String RED_COLOR="#F44336";
    public PatternLockerView(Context context) {
        super(context);
        init(null);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.PatternLockerView);
        init(typedArray);
    }

    private void init(TypedArray typedArray) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHitList = new ArrayList<>();
        mPointNum = 9;
        mEdgeN = (int) Math.sqrt(mPointNum);
        mLineColor = Color.parseColor(BLUE_COLOR);
        mHitColor = Color.parseColor(BLUE_COLOR);
        mCircleColor = Color.parseColor(BLUE_COLOR);
        mErrorColor = Color.parseColor(RED_COLOR);
        mPadding = NOT_SET;
        mCircleR = NOT_SET;
        mHitR = NOT_SET;
        mCircleWidth = 3;
        mLineWidth = 8;
        mAutoClean = true;
        mCleanDuration = 500;
        mCurrentIndex = -1;
        if(typedArray!=null){
            mPointNum=typedArray.getInteger(R.styleable.PatternLockerView_point_num,9);
            mLineColor=typedArray.getColor(R.styleable.PatternLockerView_line_color,Color.parseColor(BLUE_COLOR));
            mHitColor=typedArray.getColor(R.styleable.PatternLockerView_hit_color,Color.parseColor(BLUE_COLOR));
            mErrorColor=typedArray.getColor(R.styleable.PatternLockerView_error_color,Color.parseColor(RED_COLOR));
            mCircleColor=typedArray.getColor(R.styleable.PatternLockerView_circle_color,Color.parseColor(BLUE_COLOR));
            mAutoClean=typedArray.getBoolean(R.styleable.PatternLockerView_auto_clean,true);
            mCleanDuration=typedArray.getInteger(R.styleable.PatternLockerView_clean_duration,500);
            mCircleWidth=typedArray.getInteger(R.styleable.PatternLockerView_circle_width,3);
            mLineWidth=typedArray.getInteger(R.styleable.PatternLockerView_line_width,8);
        }
        mStatus = status.FREE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制底面圆圈
        onDrawBase(canvas);
        //绘制已触摸圆
        onDrawHitCircle(canvas);
        //绘制过程线段
        onDrawLine(canvas);
    }

    private void onDrawLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
        try {
            if (mHitList.size() > 1) {
                //绘制圆之间的线段
                int[] lastXY = new int[2];
                int[] currXY = new int[2];
                getCenterXYByIndex(mHitList.get(0), lastXY);
                for (int i = 1; i < mHitList.size(); i++) {
                    getCenterXYByIndex(mHitList.get(i), currXY);
                    if(mStatus==status.ERROR)
                        mPaint.setColor(mErrorColor);
                    else
                        mPaint.setColor(mLineColor);
                    canvas.drawLine(lastXY[0], lastXY[1], currXY[0], currXY[1], mPaint);
                    lastXY[0] = currXY[0];
                    lastXY[1] = currXY[1];
                }
            }
            //绘制圆到当前触摸点的线段
            if (mHitList.size() != 0 && mStatus == status.ING) {
                int[] XY = new int[2];
                getCenterXYByIndex(mCurrentIndex, XY);
                canvas.drawLine(XY[0], XY[1], mTouchX, mTouchY, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDrawHitCircle(Canvas canvas) {
        try {
            int[] XY = new int[2];
            mPaint.setStyle(Paint.Style.FILL);
            for (int index : mHitList) {
                getCenterXYByIndex(index, XY);
                if (mStatus == status.ERROR)
                    mPaint.setColor(mErrorColor);
                else
                    mPaint.setColor(mHitColor);
                canvas.drawCircle(XY[0], XY[1], mHitR, mPaint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //绘制底部n个圆
    private void onDrawBase(Canvas canvas) {
        try {
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mCircleWidth);
            int[] XY = new int[2];
            for (int i = 0; i < mPointNum; i++) {
                getCenterXYByIndex(i, XY);
                if (mStatus == status.ERROR&&mHitList.contains(i)) {
                    mPaint.setColor(mErrorColor);
                }
                else {
                    mPaint.setColor(mCircleColor);
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

    //根据坐标检测有无触摸到点
    private boolean isHitPoint() {
        if (checkOut()) return false;
        int[] XY = new int[2];
        try {
            int index = getCenterXYByPoint(mTouchX, mTouchY, XY);
            int distance = getDistanceToCenter(mTouchX, mTouchY, XY);
            if (distance <= mCircleR && !mHitList.contains(index)) {
                mCurrentIndex = index;
                return true;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private int getCenterXYByPoint(int x, int y, int[] XY) throws Exception {
        if (XY.length < 2)
            throw new Exception("需传入长度大于等于2的数组");
        int j = x / mOneSize;
        int i = y / mOneSize;
        XY[0] = mOneSize * j + mOneR;
        XY[1] = mOneSize * i + mOneR;
        return mEdgeN * i + j;
    }

    private int getDistanceToCenter(int x, int y, int[] XY) throws Exception {
        if (XY.length < 2)
            throw new Exception("需传入长度大于等于2的数组");
        return (int) Math.sqrt(Math.pow(x - XY[0], 2) + Math.pow(y - XY[1], 2));
    }

    public void clean() {
        mHitList.clear();
        mStatus=status.FREE;
        invalidate();
        if (mListener != null)
            mListener.onClear();
    }

    private boolean checkOut() {
        if (mTouchX > mSize || mTouchY > mSize || mTouchX < 0 || mTouchY < 0)
            return true;
        return false;
    }

    public void setErrorStatus() {
        LogUtil.logD("1状态  "+mStatus);
        mStatus = status.ERROR;
        LogUtil.logD("2状态  "+mStatus);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mTouchX = (int) event.getX();
        mTouchY = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (checkOut()||mStatus!=status.FREE) {
                    return false;
                }
            case MotionEvent.ACTION_MOVE:
                mStatus = status.ING;
                if (isHitPoint()) {
                    mHitList.add(mCurrentIndex);
                    if (mListener != null) {
                        if (mHitList.size() == 1)
                            mListener.onStart(mHitList);
                        else
                            mListener.onChange(mHitList);
                    }
                }
                //重绘
                if (mHitList.size() != 0) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mStatus == status.ING) {
                    mStatus = status.COMPLETE;
                    invalidate();
                    if (mListener != null) mListener.onEnd(mHitList);
                }
                if (mAutoClean) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mStatus=status.FREE;
                            clean();
                        }
                    }, mCleanDuration);
                }
                break;
        }
        return true;
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

    public PatternLockerStatusListener getmListener() {
        return mListener;
    }

    public void setmListener(PatternLockerStatusListener mListener) {
        this.mListener = mListener;
    }
}
