package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.User;

public interface LoginContract {
    interface ILoginView extends IView{
        void onLoginSuccess(User user);
        void onLoginFailed(String msg);
        //填写信息有误
        void onMessageError(String msg);
    }
    interface ILoginPresenter extends IPresenter<ILoginView,ILoginModel>{
        void login(String phone,String pwd);
    }
    interface ILoginModel extends IModel{
        void login(String pwd,String phone, ICallbackListener<User> iCallbackListener);
    }
}
