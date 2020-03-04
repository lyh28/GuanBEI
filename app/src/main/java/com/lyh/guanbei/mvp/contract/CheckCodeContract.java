package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.CheckCode;

public interface CheckCodeContract {
    interface ICheckCodeView extends IView{
        void sendCheckCodeFailed(String msg);
        void sendCheckCodeSuccess();
        void checkCodeSuccess();
        void checkCodeFailed(String msg);
    }
    interface ICheckCodePresenter extends IPresenter<ICheckCodeView,ICheckCodeModel>{
        void sendCheckCode(String phone);
        void checkCheckCode(String phone,int checkCode);
    }
    interface ICheckCodeModel extends IModel{
        void sendCheckCode(String phone, ICallbackListener<String> iCallbackListener);
        void checkCheckCode(CheckCode checkCode, ICallbackListener<String> iCallbackListener);
    }
}
