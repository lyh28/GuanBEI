package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Setting;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternLockerStatusListener;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternLockerView;
import com.lyh.guanbei.util.Util;

import java.util.List;

public class VerifyLockerActivity extends BaseActivity implements PatternLockerStatusListener {
    private String mPwd;
    private Setting setting;
    private PatternLockerView mLockerView;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_verify_locker;
    }

    @Override
    protected void initUi() {
        mLockerView=findViewById(R.id.patternlockerview);
        mLockerView.setmListener(this);
    }

    @Override
    protected void init() {
        setting=CustomSharedPreferencesManager.getInstance().getUser().getSetting();
        mPwd=setting.getPattern_pwd();
    }

    @Override
    public void onStart(List<Integer> points) {

    }

    @Override
    public void onChange(List<Integer> points) {

    }

    @Override
    public void onEnd(List<Integer> points) {
        String pwd=Util.getDataFromList(points);
        if(mPwd.equals(pwd)){
            setting.setPattern_pwd("").save();
            finish();
        }else{
            mLockerView.setErrorStatus();
            mLockerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLockerView.clean();
                }
            },500);
        }
    }

    @Override
    public void onClear() {

    }

    @Override
    public void createPresenters() {

    }
}
