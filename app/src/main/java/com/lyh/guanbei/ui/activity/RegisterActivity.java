package com.lyh.guanbei.ui.activity;

import android.view.View;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.jpush.PushMessageReceiver;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.RegisterContract;
import com.lyh.guanbei.mvp.presenter.RegisterPresenter;

import cn.jpush.android.api.JPushInterface;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, RegisterContract.IRegisterView {
    private RegisterPresenter mRegisterPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initUi() {
        findViewById(R.id.activity_register_back).setOnClickListener(this);
//        findViewById(R.id.activity_register_back)
    }

    @Override
    protected void init() {

    }

    @Override
    public void onRegisterSuccess(User user) {
        JPushInterface.setAlias(this, PushMessageReceiver.USERALIAS,user.getUser_id()+"");
        CustomSharedPreferencesManager.getInstance().saveUser(user);
//        DBManager.getInstance().getDaoSession().getBookDao(
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onRegisterFailed(String msg) {

    }

    @Override
    public void onMessageError(String msg) {

    }

    @Override
    public void createPresenters() {
        mRegisterPresenter=new RegisterPresenter();
        addPresenter(mRegisterPresenter);
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    protected boolean isLocked() {
        return false;
    }
}
