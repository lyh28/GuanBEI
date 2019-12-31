package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.RegisterContract;

public class RegisterModel implements RegisterContract.IRegisterModel {
    @Override
    public void register(User user, final ICallbackListener iCallbackListener) {
        APIManager.register(user, new BaseObscriber<User>() {
            @Override
            protected void onSuccess(User data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
}
