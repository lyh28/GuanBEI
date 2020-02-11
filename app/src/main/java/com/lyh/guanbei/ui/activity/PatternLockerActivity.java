package com.lyh.guanbei.ui.activity;

import android.animation.AnimatorSet;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternIndicatorView;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternLockerStatusListener;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternLockerView;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;

import java.util.ArrayList;
import java.util.List;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

public class PatternLockerActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTips;
    private TextView mReset;
    private PatternLockerView mLockerView;
    private PatternIndicatorView mIndicatorView;
    private String mHit;
    private final int minN=4;
    private boolean isFirst;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pattern_locker;
    }

    @Override
    protected void initUi() {
        mLockerView = findViewById(R.id.patternlockerview);
        mIndicatorView = findViewById(R.id.patternindicatorview);
        mTips=findViewById(R.id.activity_pattern_tips);
        mReset=findViewById(R.id.activity_pattern_reset);
        findViewById(R.id.activity_pattern_back).setOnClickListener(this);
        mReset.setOnClickListener(this);
        mLockerView.setmListener(new PatternLockerStatusListener() {
            @Override
            public void onStart(List<Integer> points) {
                mIndicatorView.updateHitPoint(points);
            }

            @Override
            public void onChange(List<Integer> points) {
                mIndicatorView.updateHitPoint(points);
            }

            @Override
            public void onEnd(List<Integer> points) {
                if(isFirst)
                    firstLocker(points);
                else
                    secondLocker(points);
            }

            @Override
            public void onClear() {

            }
        });
    }
    private void firstLocker(List<Integer> points){
        if (points.size() < minN) {
            mLockerView.setErrorStatus();
            mLockerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLockerView.clean();
                }
            },1000);
            onError("最少连接4个点，请重新输入");
        }else {
            isFirst=false;
            mHit=Util.getDataFromList(points);
            onTextChange("再次绘制解锁图案");
            mLockerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLockerView.clean();
                }
            },500);
        }
    }

    private void secondLocker(List<Integer> points){
        if(mHit.equals(Util.getDataFromList(points))){
            //相等图案
            CustomSharedPreferencesManager.getInstance().getUser().getSetting().setPattern_pwd(mHit).save();
            Toast.makeText(this,"手势密码设置成功",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            onError("与上一次绘制不一致，请重新绘制");
            mLockerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLockerView.clean();
                }
            },500);
            mReset.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_pattern_back:
                finish();
                break;
            case R.id.activity_pattern_reset:
                isFirst=true;
                onTextChange("绘制解锁图案");
                mIndicatorView.clean();
                mLockerView.clean();
                mReset.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    protected void init() {
        mHit="";
        isFirst=true;
    }
    private void onError(String msg){
        mTips.setTextColor(Color.RED);
        mTips.setText(msg);
        mTips.startAnimation(getTextTranslateAnimSet());
    }
    private void onTextChange(String msg){
        mTips.setTextColor(Color.BLACK);
        mTips.setText(msg);
    }
    private AnimationSet getTextTranslateAnimSet(){
        AnimationSet animationSet=new AnimationSet(false);
        animationSet.addAnimation(getTextTranslateAnim(0,10,50,0));
        animationSet.addAnimation(getTextTranslateAnim(10,-10,100,50));
        animationSet.addAnimation(getTextTranslateAnim(-10,10,100,150));
        animationSet.addAnimation(getTextTranslateAnim(10,0,50,250));
        animationSet.setInterpolator(new LinearInterpolator());
        return animationSet;
    }
    private Animation getTextTranslateAnim(int fromX,int toX,int duration,int offset){
        Animation animation=new TranslateAnimation(fromX,toX,0,0);
        animation.setStartOffset(offset);
        animation.setDuration(duration);
        return animation;
    }
    @Override
    public void createPresenters() {

    }
}
