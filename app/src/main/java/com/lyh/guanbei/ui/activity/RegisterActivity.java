package com.lyh.guanbei.ui.activity;

import android.view.View;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;

public class RegisterActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initUi() {
        findViewById(R.id.activity_register_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void createPresenters() {

    }
}
