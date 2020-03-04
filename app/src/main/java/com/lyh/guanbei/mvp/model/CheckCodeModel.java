package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.CheckCode;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.CheckCodeContract;

public class CheckCodeModel implements CheckCodeContract.ICheckCodeModel {
    @Override
    public void sendCheckCode(String phone, final ICallbackListener<String> iCallbackListener) {
        APIManager.sendCheckCode(phone, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void checkCheckCode(CheckCode checkCode,final ICallbackListener<String> iCallbackListener) {
        APIManager.checkCheckCode(checkCode, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
}
