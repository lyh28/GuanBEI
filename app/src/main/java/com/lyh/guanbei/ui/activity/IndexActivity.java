package com.lyh.guanbei.ui.activity;

import android.view.View;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;

public class IndexActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_index;
    }

    @Override
    protected void initUi() {
        findViewById(R.id.activity_index_toLogin).setOnClickListener(this);
        findViewById(R.id.activity_index_toRegister).setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void createPresenters() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_index_toLogin:
                startActivity(LoginActivity.class);
                break;
            case R.id.activity_index_toRegister:
                startActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
