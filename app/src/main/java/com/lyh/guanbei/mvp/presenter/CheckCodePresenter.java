package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.CheckCode;
import com.lyh.guanbei.mvp.contract.CheckCodeContract;
import com.lyh.guanbei.mvp.model.CheckCodeModel;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.LogUtil;

public class CheckCodePresenter extends BasePresenter<CheckCodeContract.ICheckCodeView, CheckCodeContract.ICheckCodeModel> implements CheckCodeContract.ICheckCodePresenter {
    @Override
    public void sendCheckCode(String phone) {
        LogUtil.logD("发送验证码");
        getmView().sendCheckCodeSuccess();
//        getmModel().sendCheckCode(phone, new ICallbackListener<String>() {
//            @Override
//            public void onSuccess(String data) {
//                if (checkAttach())
//                    getmView().sendCheckCodeSuccess();
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                if (checkAttach())
//                    getmView().sendCheckCodeFailed(msg);
//            }
//        });
    }

    @Override
    public void checkCheckCode(String phone, int checkCode) {
        LogUtil.logD("验证验证码");
        getmView().checkCodeSuccess();

//        CheckCode code = new CheckCode(phone, checkCode, DateUtil.getNowDateTimeWithSecond());
//        getmModel().checkCheckCode(code, new ICallbackListener<String>() {
//            @Override
//            public void onSuccess(String data) {
//                if (checkAttach())
//                    getmView().checkCodeSuccess();
//            }
//
//            @Override
//            public void onFailed(String msg) {
//                if (checkAttach())
//                    getmView().checkCodeFailed(msg);
//            }
//        });
    }

    @Override
    public CheckCodeModel createModel() {
        return new CheckCodeModel();
    }
}
