package com.lyh.guanbei.mvp.contract;

import android.net.Uri;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.User;

import okhttp3.MultipartBody;

public interface UpdateUserContract {
    interface IUpdateUserView extends IView{
        void onUpdateFailed(String msg);
        void onUpdateSuccess(User user);
    }
    interface IUpdateUserPresenter extends IPresenter<IUpdateUserView,IUpdateUserModel>{
        void updateIcon(Uri iconUri);
        void updateOther(User user);
    }
    interface IUpdateUserModel extends IModel{
        void updateIcon(long userId, MultipartBody.Part icon, ICallbackListener<User> iCallbackListener);
        void updateOther(User user, ICallbackListener<User> iCallbackListener);
    }
}
