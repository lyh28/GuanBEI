package com.lyh.guanbei.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.jpush.android.api.JPushInterface;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.jpush.PushMessageReceiver;
import com.lyh.guanbei.manager.ActivityManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternLockerStatusListener;
import com.lyh.guanbei.ui.widget.pattenerLocker.PatternLockerView;
import com.lyh.guanbei.util.Util;

import java.util.List;

public class UnLockActivity extends BaseActivity implements PatternLockerStatusListener , View.OnClickListener {
    private ImageView mIcon;
    private TextView mPhone;
    private TextView mTips;
    private PatternLockerView mLockerView;
    private TextView mForget;
    private TextView mChange;

    private String mPwd;
    private User mUser;
    private int count;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_unlock;
    }

    @Override
    protected void initUi() {
        mIcon = findViewById(R.id.activity_unlock_icon);
        mPhone = findViewById(R.id.activity_unlock_phone);
        mTips = findViewById(R.id.activity_unlock_tips);
        mLockerView = findViewById(R.id.activity_unlock_lockerview);
        mForget = findViewById(R.id.activity_unlock_forget);
        mChange = findViewById(R.id.activity_unlock_change);

        mLockerView.setmListener(this);
        mForget.setOnClickListener(this);
        mChange.setOnClickListener(this);

    }

    @Override
    protected void init() {
        count = 5;
        CustomSharedPreferencesManager sharedPreferencesManager = CustomSharedPreferencesManager.getInstance();
        mUser = sharedPreferencesManager.getUser();
        mPwd = mUser.getSetting().getPattern_pwd();
        Glide.with(this).load(mUser.getUser_icon()).error(R.drawable.defaulticon).into(mIcon);
        mPhone.setText(mUser.getUser_phone());
    }

    @Override
    public void createPresenters() {

    }

    @Override
    public void onStart(List<Integer> points) {

    }

    @Override
    public void onChange(List<Integer> points) {

    }

    @Override
    public void onEnd(List<Integer> points) {
        String point = Util.getDataFromList(points);
        if (point.equals(mPwd)) {
            //密码相同
            finish();
        } else {
            mLockerView.setErrorStatus();
            mLockerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLockerView.clean();
                }
            }, 500);
            count--;
            onError();
        }
    }

    @Override
    public void onClear() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_unlock_change:
                //返回登录界面
                break;
            case R.id.activity_unlock_forget:
                //进入到密码验证
                //清除手势密码
                CustomSharedPreferencesManager.getInstance().getUser().getSetting().setPattern_pwd("").save();
                //清除当前登录用户，需要重新登录
                CustomSharedPreferencesManager.getInstance().saveUser(null);
                JPushInterface.deleteAlias(this, PushMessageReceiver.USERALIAS);
                ActivityManager.getInstance().finishAll();
                startActivity(LoginActivity.class);
                break;
        }
    }

    private void onError() {
        if(count==4) {
            mTips.setVisibility(View.VISIBLE);
            mPhone.setVisibility(View.VISIBLE);
        }
        mTips.setText(wrapTips());
    }

    private String wrapTips(){
        return "密码错误，还可以再输入"+count+"次";
    }
    @Override
    public void onBackPressed() {
        //将应用退至后台
        moveTaskToBack(true);
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
