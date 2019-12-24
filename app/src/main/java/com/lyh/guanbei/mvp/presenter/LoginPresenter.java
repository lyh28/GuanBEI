package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.LoginContract;
import com.lyh.guanbei.mvp.model.LoginModel;
import com.lyh.guanbei.util.NetUtil;

public class LoginPresenter extends BasePresenter<LoginContract.ILoginView, LoginContract.ILoginModel> implements LoginContract.ILoginPresenter {
    @Override
    public void login(String phone, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            getmView().onMessageError("手机号为空");
        } else if (TextUtils.isEmpty(pwd)) {
            getmView().onMessageError("密码为空");
        } else {
            if(NetUtil.checkNetWorkUnavailable(getmContext())){
                return;
            }
            getmModel().login(pwd, phone, new ICallbackListener<UserBean>() {
                @Override
                public void onSuccess(UserBean data) {
                    if(checkAttach()) {
                        CustomSharedPreferencesManager.getInstance(getmContext()).saveUser(data);
                        getmView().onLoginSuccess(data);
                    }
                }

                @Override
                public void onFailed(String msg) {
                    if(checkAttach())
                        getmView().onLoginFailed(msg);
                }
            });
        }
    }

    @Override
    public LoginContract.ILoginModel createModel() {
        return new LoginModel();
    }
}
