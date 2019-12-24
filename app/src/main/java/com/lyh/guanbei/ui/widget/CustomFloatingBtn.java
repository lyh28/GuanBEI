package com.lyh.guanbei.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lyh.guanbei.R;
import com.lyh.guanbei.util.LogUtil;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomFloatingBtn extends FrameLayout {
    private CircleBorder circleBorder;
    private TextView center;
    private TextView amount;
    private Listener listener;

    private int touchSlop;
    private int lastX;
    private int lastY;
    private int status;     //0代表移动   1代表点击
    private int w;
    private int h;
    private int screenw;
    private int screenh;
    private int statusH;
    private int minMargin;
    private static final String TOUCH_COLOR="#e98f36";
    private static final String UNTOUCH_COLOR="#8a8a8a";

    public CustomFloatingBtn(@NonNull Context context) {
        super(context);
    }

    public CustomFloatingBtn(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w=w;
        this.h=h;
        //设定初始位置
        setY(screenh/4*3);
        setX(screenw-minMargin-w);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.custom_floating_btn,this);
        circleBorder=findViewById(R.id.custom_float_circleborder);
        center=findViewById(R.id.custom_float_text);
        amount=findViewById(R.id.custom_float_amount);
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        screenh= QMUIDisplayHelper.getScreenHeight(context);
        screenw=QMUIDisplayHelper.getScreenWidth(context);
        minMargin=QMUIDisplayHelper.dpToPx(20);
        statusH=QMUIDisplayHelper.getStatusBarHeight(context);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int currX = (int) event.getRawX();
        int currY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = currX;
                lastY = currY;
                status=1;
                onTouch();
                break;
            case MotionEvent.ACTION_MOVE:
                if (getDistance(currX - lastX, currY - lastY) > touchSlop) {
                    int x=currX-w/2;
                    int y=currY-h/2;
                    //判断有无出边界
                    if(x<0)
                        x=0;
                    if(x>screenw-w)
                        x=screenw-w;
                    if(y<statusH)
                        y=statusH;
                    if(y>screenh-h)
                        y=screenh-h;
                    setX(x);
                    setY(y);
                    status=0;
                }else{
                    status=1;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (status ==1) {
                    if(listener!=null)
                        listener.onClick();
                }else{
                    if(getX()<screenw/2)
                        setX(minMargin);
                    else
                        setX(screenw-minMargin-w);
                }
                onRelease();
                break;
        }
        return true;
    }
    private int getDistance(int a, int b) {
        return (int) Math.sqrt((Math.pow(a, 2) + Math.pow(b, 2)));
    }
    private void onTouch(){
        circleBorder.setColor(Color.parseColor(TOUCH_COLOR));
        center.setTextColor(Color.parseColor(TOUCH_COLOR));
        amount.setTextColor(Color.parseColor(TOUCH_COLOR));
    }

    private void onRelease(){
        circleBorder.setColor(Color.parseColor(UNTOUCH_COLOR));
        center.setTextColor(Color.parseColor(UNTOUCH_COLOR));
        amount.setTextColor(Color.parseColor(UNTOUCH_COLOR));
    }
    public void setAmount(int n){
        amount.setText(n+"");
    }
    public void setListener(Listener listener){
        this.listener=listener;

    }
    public interface Listener{
        void onClick();
    }
}
