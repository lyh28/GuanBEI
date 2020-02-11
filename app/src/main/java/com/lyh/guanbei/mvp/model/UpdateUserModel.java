package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;

import okhttp3.MultipartBody;

public class UpdateUserModel implements UpdateUserContract.IUpdateUserModel {
    @Override
    public void updateIcon(long userId, MultipartBody.Part icon, final ICallbackListener<User> iCallbackListener) {
        APIManager.updateIcon(userId, icon, new BaseObscriber<User>() {
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

    @Override
    public void updateOther(User user, final ICallbackListener<User> iCallbackListener) {
        APIManager.update(user, new BaseObscriber<User>() {
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
