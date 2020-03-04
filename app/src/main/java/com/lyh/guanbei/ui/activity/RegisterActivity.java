package com.lyh.guanbei.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.CheckCodeContract;
import com.lyh.guanbei.mvp.contract.RegisterContract;
import com.lyh.guanbei.mvp.presenter.CheckCodePresenter;
import com.lyh.guanbei.mvp.presenter.RegisterPresenter;
import com.lyh.guanbei.util.KeyBoardUtil;
import com.lyh.guanbei.util.LogUtil;

import androidx.annotation.Nullable;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, CheckCodeContract.ICheckCodeView {
    private EditText mPhone;

    private Button mRegister;
    private CheckCodePresenter mCheckCodePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initUi() {
        mPhone = findViewById(R.id.activity_register_phone);
        mRegister = findViewById(R.id.activity_register_register);
        findViewById(R.id.activity_register_back).setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(checkPhone()) {
                    KeyBoardUtil.hideSystemKeyboard(RegisterActivity.this);
                    mRegister.setEnabled(true);
                }
                else
                    mRegister.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void init() {
    }

    @Override
    public void createPresenters() {
        mCheckCodePresenter=new CheckCodePresenter();
        addPresenter(mCheckCodePresenter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_register_back:
                finish();
                break;
            case R.id.activity_register_register:
                mCheckCodePresenter.sendCheckCode(mPhone.getText().toString());
                break;
        }
    }

    @Override
    public void sendCheckCodeFailed(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void sendCheckCodeSuccess() {
        //先验证密码  大于等于7位  验证账号
        Intent intent = new Intent(this, CheckCodeActivity.class);
        intent.putExtra("phone", mPhone.getText().toString());
        intent.putExtra("type", 0);
        startActivity(intent);
    }
    @Override
    public void checkCodeSuccess() {}
    @Override
    public void checkCodeFailed(String msg) {}

    public boolean checkPhone() {
        return mPhone.getText().length() == 11;
    }

    @Override
    protected boolean isLocked() {
        return false;
    }
}
