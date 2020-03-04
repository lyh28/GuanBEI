package com.lyh.guanbei.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.mvp.contract.CheckCodeContract;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;
import com.lyh.guanbei.mvp.presenter.CheckCodePresenter;
import com.lyh.guanbei.mvp.presenter.UpdateUserPresenter;
import com.lyh.guanbei.util.LogUtil;

public class CheckCodeActivity extends BaseActivity implements View.OnClickListener, CheckCodeContract.ICheckCodeView{
    private EditText code1;
    private EditText code2;
    private EditText code3;
    private EditText code4;
    private EditText code5;
    private EditText code6;
    private TextView mRestart;
    private TextView mPhone;
    private Button mBtn;

    private int CD;         //60s冷却
    private int type;       //类型  注册用户用还是修改密码用  0  1
    private String mPhoneStr;
    private final String RESTART_HEAD = "重发验证码";
    private final int TO_SETPWD=101;
    private CheckCodePresenter mCheckCodePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_code;
    }

    @Override
    protected void initUi() {
        findViewById(R.id.page_back).setOnClickListener(this);
        mRestart = findViewById(R.id.activity_check_code_restart);
        mPhone = findViewById(R.id.activity_check_code_phone);
        mBtn=findViewById(R.id.activity_check_code_btn);
        mBtn.setOnClickListener(this);
        mRestart.setOnClickListener(this);
        code1 = findViewById(R.id.activity_check_code1);
        code2 = findViewById(R.id.activity_check_code2);
        code3 = findViewById(R.id.activity_check_code3);
        code4 = findViewById(R.id.activity_check_code4);
        code5 = findViewById(R.id.activity_check_code5);
        code6 = findViewById(R.id.activity_check_code6);

        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) code2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) code3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) code4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) code5.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        code5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) code6.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.page_back:
                finish();
            case R.id.activity_check_code_btn:
                int code=wrapCode().equals("")?0:Integer.parseInt(wrapCode());
                mCheckCodePresenter.checkCheckCode(mPhoneStr, code);
                break;
            case R.id.activity_check_code_restart:
                if (CD == 0)
                    sendCode();
                break;
        }
    }

    @Override
    protected void init() {
        CD = 60;
        setRestart();
        Bundle bundle = getIntentData();
        mPhoneStr = bundle.getString("phone");
        type = bundle.getInt("type");
        boolean isSend=bundle.getBoolean("send",false);
        if(isSend)  sendCode();
        if(type==0){
            mBtn.setText("验证");
        }else if(type==1){
            mBtn.setText("重设密码");
        }
        mPhone.setText(wrapPhone(mPhoneStr));
    }

    private void sendCode() {
        mCheckCodePresenter.sendCheckCode(mPhoneStr);
        CD = 60;
        setRestart();
    }

    private String wrapPhone(String phone) {
        return phone.substring(0, 3) + "*****" + phone.substring(9);
    }

    private String wrapRestart() {
        if (CD <= 0) {
            mRestart.setTextColor(getResources().getColor(R.color.qmui_config_color_blue));
            return RESTART_HEAD;
        }
        mRestart.setTextColor(getResources().getColor(R.color.colorGray));
        return CD + "秒后" + RESTART_HEAD;
    }

    private String wrapCode() {
        return getText(code1) + getText(code2) + getText(code3) + getText(code4) + getText(code5) + getText(code6);
    }

    private String getText(EditText editText) {
        return editText.getText().toString();
    }

    private void setRestart() {
        mRestart.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (CD > 1) {
                    setRestart();
                }
                CD--;
                mRestart.setText(wrapRestart());
            }
        }, 1000);
    }

    @Override
    public void createPresenters() {
        mCheckCodePresenter = new CheckCodePresenter();
        addPresenter(mCheckCodePresenter);
    }

    @Override
    public void sendCheckCodeFailed(String msg) {
        Toast.makeText(this, "发送验证码失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendCheckCodeSuccess() {
        //设置重发验证码的时间
        setRestart();
        Toast.makeText(this, "发送验证码成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void checkCodeSuccess() {
        //检查验证码
        //返回
        Intent intent=new Intent(this, SetPwdActivity.class);
        intent.putExtra("phone",mPhoneStr);
        intent.putExtra("type",type);
        startActivityForResult(intent,TO_SETPWD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==TO_SETPWD){
            if(resultCode==RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void checkCodeFailed(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected boolean isLocked() {
        return false;
    }
}
