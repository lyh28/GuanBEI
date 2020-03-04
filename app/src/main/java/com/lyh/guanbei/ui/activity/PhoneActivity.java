package com.lyh.guanbei.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.mvp.contract.CheckCodeContract;
import com.lyh.guanbei.mvp.presenter.CheckCodePresenter;

public class PhoneActivity extends BaseActivity implements View.OnClickListener, CheckCodeContract.ICheckCodeView {
    private EditText mPhone;
    private Button mNext;

    private CheckCodePresenter mCheckCodePresenter;

    private final int TO_CHECKCODE = 101;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_phone;
    }

    @Override
    protected void initUi() {
        mPhone = findViewById(R.id.activity_phone_phone);
        mNext = findViewById(R.id.activity_phone_next);
        mNext.setOnClickListener(this);
        findViewById(R.id.page_back).setOnClickListener(this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page_back:
                finish();
                break;
            case R.id.activity_phone_next:
                if (checkPhone())
                    mCheckCodePresenter.sendCheckCode(mPhone.getText().toString());
                break;
        }
    }

    private boolean checkPhone() {
        return mPhone.getText().length() == 11;
    }

    @Override
    public void sendCheckCodeFailed(String msg) {
        Toast.makeText(this, "号码不存在", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendCheckCodeSuccess() {
        Bundle bundle = new Bundle();
        bundle.putString("phone", mPhone.getText().toString());
        bundle.putInt("type", 1);
        Intent intent = new Intent(this, CheckCodeActivity.class);
        intent.putExtras(bundle);
        startActivityForResult(intent, TO_CHECKCODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == TO_CHECKCODE) {
            if (resultCode == RESULT_OK)
                finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void checkCodeSuccess() {
    }

    @Override
    public void checkCodeFailed(String msg) {
    }

    @Override
    public void createPresenters() {
        mCheckCodePresenter = new CheckCodePresenter();
        addPresenter(mCheckCodePresenter);
    }
}
