package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.RegisterContract;
import com.lyh.guanbei.mvp.model.RegisterModel;
import com.lyh.guanbei.util.NetUtil;

public class RegisterPresenter extends BasePresenter<RegisterContract.IRegisterView, RegisterContract.IRegisterModel> implements RegisterContract.IRegisterPresenter {
    @Override
    public RegisterContract.IRegisterModel createModel() {
        return new RegisterModel();
    }

    @Override
    public void register(String name, String phone, String pwd) {
        if (TextUtils.isEmpty(phone)) {
            getmView().onMessageError("手机号为空");
        } else if (TextUtils.isEmpty(pwd)) {
            getmView().onMessageError("密码为空");
        } else {
            if (NetUtil.checkNetWorkUnavailable(getmContext())) {
                return;
            }
            getmModel().register(createUserBean(name, pwd, phone), new ICallbackListener<User>() {
                @Override
                public void onSuccess(User data) {
                    DBManager.getInstance().getDaoSession().getUserDao().insertOrReplace(data);
                    if (checkAttach()) {
                        CustomSharedPreferencesManager.getInstance(getmContext()).saveUser(data);
                        getmView().onRegisterSuccess(data);
                    }
                }

                @Override
                public void onFailed(String msg) {
                    if (checkAttach())
                        getmView().onRegisterFailed(msg);
                }
            });
        }
    }

    private User createUserBean(String name, String pwd, String phone) {
        User user = new User();
        user.setUser_phone(phone);
        user.setUser_pwd(pwd);
        user.setUser_name(name);
        return user;
    }
}
