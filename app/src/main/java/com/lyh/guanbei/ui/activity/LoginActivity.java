package com.lyh.guanbei.ui.activity;

import cn.jpush.android.api.JPushInterface;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lyh.guanbei.R;
import com.lyh.guanbei.base.BaseActivity;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.DBManager;
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
    private User user;
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
        findViewById(R.id.activity_login_pass).setOnClickListener(this);
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
    public void onLoginSuccess(User user) {
        //设置别名
        this.user=user;
        JPushInterface.setAlias(this, PushMessageReceiver.USERALIAS, user.getUser_id()+"");
        String bookids= user.getBook_id();
        List<Long> list= Util.getLongFromData(bookids);
        mQueryBookPresenter.queryBookServer(list);
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
    public void showBook(List<Book> list) {
        if(isFirst) {
            CustomSharedPreferencesManager preferencesManager=CustomSharedPreferencesManager.getInstance();
            if(list.size()==0) {
                preferencesManager.saveCurrBookId(-1);
            }else{
                preferencesManager.saveCurrBookId(list.get(0).getLocal_id());
            }
            String local_book_id="";
            for(Book book:list)
                local_book_id=Util.addToData(book.getLocal_id(),local_book_id);
            user.setLocal_book_id(local_book_id);
            preferencesManager.saveUser(user);
            DBManager.getInstance().getDaoSession().getUserDao().insertOrReplace(user);
            isFirst = false;
            mDialog.dismiss();
            startActivity(MainActivity.class);
        }
    }

    @Override
    public void queryBookFailed() {
        if(isFirst){
            CustomSharedPreferencesManager preferencesManager=CustomSharedPreferencesManager.getInstance();
            preferencesManager.saveCurrBookId(-1);
            preferencesManager.saveUser(user);
            DBManager.getInstance().getDaoSession().getUserDao().insertOrReplace(user);
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
            case R.id.activity_login_pass:
                //设置userId为-1
                user=new User();
                user.setUser_id(-1);
                //可能需要删除上一个userId为-1留下的数据
                CustomSharedPreferencesManager.getInstance().saveUser(user);
                DBManager.getInstance().getDaoSession().getUserDao().insertOrReplace(user);
                startActivity(MainActivity.class);
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
