package com.lyh.guanbei.ui.activity;

import android.os.Handler;
import android.os.Message;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.common.MainActivity;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;

public class WelcomeActivity extends BaseActivity {
    private static final int LOGIN_CODE = 1;
    private static final int MAIN_CODE = 2;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MAIN_CODE) {
                startActivity(MainActivity.class);
                finish();
            } else if (msg.what == LOGIN_CODE) {
                startActivity(IndexActivity.class);
                finish();
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initUi() {
    }

    @Override
    protected void init() {
        //得到登录账户
        final CustomSharedPreferencesManager preferencesManager = CustomSharedPreferencesManager.getInstance(this);
        if (preferencesManager.getUser() != null) {
            mHandler.sendEmptyMessageDelayed(MAIN_CODE, 1500);
        } else {
            mHandler.sendEmptyMessageDelayed(LOGIN_CODE, 1500);
        }
    }

    @Override
    public void createPresenters() {

    }
}
