package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.RegisterContract;

public class RegisterModel implements RegisterContract.IRegisterModel {
    @Override
    public void register(UserBean userBean, final ICallbackListener iCallbackListener) {
        APIManager.register(userBean, new BaseObscriber<UserBean>() {
            @Override
            protected void onSuccess(UserBean data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
}
