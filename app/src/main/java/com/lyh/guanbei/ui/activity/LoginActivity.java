package com.lyh.guanbei.ui.activity;

import cn.jpush.android.api.JPushInterface;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.jpush.PushMessageReceiver;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.LoginContract;
import com.lyh.guanbei.mvp.contract.QueryBookContract;
import com.lyh.guanbei.mvp.presenter.LoginPresenter;
import com.lyh.guanbei.mvp.presenter.QueryBookPresenter;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.ILoginView, QueryBookContract.IQueryBookView {
    private EditText mPhone;
    private EditText mPwd;
    private Button mLogin;

    private QueryBookPresenter mQueryBookPresenter;
    private LoginPresenter mLoginPresenter;

    private QMUITipDialog mDialog;
    private boolean isFirst;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initUi() {
        mPhone = findViewById(R.id.activity_login_phone);
        mPwd = findViewById(R.id.activity_login_pwd);
        mLogin = findViewById(R.id.activity_login_login);
        mLogin.setOnClickListener(this);
        findViewById(R.id.activity_login_back).setOnClickListener(this);
        findViewById(R.id.activity_login_forget).setOnClickListener(this);

        mDialog=new QMUITipDialog.Builder(this)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //检测到11位时把焦点跳到密码中
                if (s.length() == 11) {
                    mPwd.requestFocus();
                } else
                    mLogin.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    mLogin.setEnabled(true);
                else
                    mLogin.setEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void init() {
        isFirst=true;
    }

    @Override
    public void onLoginSuccess(UserBean userBean) {
        //设置别名
        JPushInterface.setAlias(this, PushMessageReceiver.USERALIAS,userBean.getUser_id()+"");
        CustomSharedPreferencesManager preferencesManager = CustomSharedPreferencesManager.getInstance(this);
        String bookids=userBean.getBook_id();
        List<Long> list= Util.getLongFromData(bookids);
        if(list.size()==0) {
            preferencesManager.saveCurrBookId(-1);
        }else{
            preferencesManager.saveCurrBookId(list.get(0));
        }
        mQueryBookPresenter.queryBook(list);
    }

    @Override
    public void onLoginFailed(String msg) {
        LogUtil.logD("登录失败 "+msg);
        mDialog.dismiss();

    }

    @Override
    public void onMessageError(String msg) {
        LogUtil.logD("信息错误");
    }

    @Override
    public void showBook(List<BookBean> list) {
        if(isFirst) {
            isFirst = false;
            mDialog.dismiss();
            startActivity(MainActivity.class);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_login:
                mDialog.show();
                String phone=mPhone.getText().toString();
                String pwd=mPwd.getText().toString();
                mLoginPresenter.login(phone,pwd);
                break;
            case R.id.activity_login_back:
                finish();
                break;
            case R.id.activity_login_forget:
                break;
        }
    }

    @Override
    public void createPresenters() {
        mLoginPresenter = new LoginPresenter();
        mQueryBookPresenter=new QueryBookPresenter();
        addPresenter(mQueryBookPresenter);
        addPresenter(mLoginPresenter);
    }
}
