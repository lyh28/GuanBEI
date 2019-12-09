package com.lyh.guanbei.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.mvp.contract.LoginContract;
import com.lyh.guanbei.mvp.contract.RegisterContract;
import com.lyh.guanbei.mvp.presenter.LoginPresenter;
import com.lyh.guanbei.mvp.presenter.RegisterPresenter;
import com.lyh.guanbei.util.LogUtil;

public class LoginActivity extends BaseActivity implements LoginContract.ILoginView, RegisterContract.IRegisterView {
    private EditText phone;
    private EditText name;
    private EditText pwd;
    private EditText phone2;
    private EditText pwd2;

    private LoginPresenter mLoginPresenter;
    private RegisterPresenter mRegisterPresenter;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initUi() {
        phone=findViewById(R.id.activity_login_phone);
        name=findViewById(R.id.activity_login_name);
        pwd=findViewById(R.id.activity_login_pwd);
        phone2=findViewById(R.id.activity_login_phone2);
        pwd2=findViewById(R.id.activity_login_pwd2);
        findViewById(R.id.activity_login_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRegisterPresenter.register(name.getText().toString(),phone.getText().toString(),pwd.getText().toString());
            }
        });
        findViewById(R.id.activity_login_bt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLoginPresenter.login(phone2.getText().toString(),pwd2.getText().toString());
            }
        });
    }

    @Override
    protected void init() {

    }

    @Override
    public void onLoginSuccess(UserBean userBean) {
        LogUtil.logD("登录成功 "+userBean);
        finish();
    }

    @Override
    public void onLoginFailed(String msg) {
        LogUtil.logD("登录失败 "+msg);
    }

    @Override
    public void onMessageError(String msg) {
        LogUtil.logD("信息错误"+msg);
    }

    @Override
    public void onRegisterSuccess(UserBean userBean) {
        LogUtil.logD("注册成功 "+userBean);
        finish();
    }

    @Override
    public void onRegisterFailed(String msg) {
        LogUtil.logD("注册失败 "+msg);
    }

    @Override
    public void createPresenters() {
        mLoginPresenter=new LoginPresenter();
        mRegisterPresenter=new RegisterPresenter();
        addPresenter(mLoginPresenter);
        addPresenter(mRegisterPresenter);
    }
}
