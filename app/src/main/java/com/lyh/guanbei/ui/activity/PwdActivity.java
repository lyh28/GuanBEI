package com.lyh.guanbei.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.LoginContract;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;
import com.lyh.guanbei.mvp.presenter.LoginPresenter;

public class PwdActivity extends BaseActivity implements LoginContract.ILoginView, View.OnClickListener {
    private EditText mPwd;
    private LoginPresenter mLoginPresenter;

    private String mPhone;
    private final int TO_SETPWD=101;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_pwd;
    }

    @Override
    protected void initUi() {
        mPwd=findViewById(R.id.activity_pwd_pwd);
        findViewById(R.id.activity_pwd_forget).setOnClickListener(this);
        findViewById(R.id.page_back).setOnClickListener(this);
        findViewById(R.id.activity_pwd_btn).setOnClickListener(this);
    }

    @Override
    protected void init() {
        mPhone=CustomSharedPreferencesManager.getInstance().getUser().getUser_phone();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.page_back:
                finish();
                break;
            case R.id.activity_pwd_forget:
                Bundle bundle=new Bundle();
                bundle.putString("phone",CustomSharedPreferencesManager.getInstance().getUser().getUser_phone());
                bundle.putInt("type",1);
                bundle.putBoolean("send",true);
                startActivity(CheckCodeActivity.class);
                finish();
                break;
            case R.id.activity_pwd_btn:
                mLoginPresenter.login(mPhone,mPwd.getText().toString());
                break;
        }
    }

    @Override
    public void onLoginSuccess(User user) {
        Intent intent=new Intent(this,SetPwdActivity.class);
        intent.putExtra("phone", mPhone);
        intent.putExtra("type",1);
        startActivityForResult(intent,TO_SETPWD);
    }

    @Override
    public void onLoginFailed(String msg) {
        Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessageError(String msg) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==TO_SETPWD){
            if(resultCode==RESULT_OK){
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void createPresenters() {
        mLoginPresenter=new LoginPresenter();
        addPresenter(mLoginPresenter);
    }

}
