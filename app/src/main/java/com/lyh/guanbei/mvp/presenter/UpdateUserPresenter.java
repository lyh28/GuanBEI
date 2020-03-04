package com.lyh.guanbei.mvp.presenter;

import android.net.Uri;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.UpdateUserContract;
import com.lyh.guanbei.mvp.model.UpdateUserModel;
import com.lyh.guanbei.util.FileUtil;
import com.lyh.guanbei.util.NetUtil;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateUserPresenter extends BasePresenter<UpdateUserContract.IUpdateUserView, UpdateUserContract.IUpdateUserModel> implements UpdateUserContract.IUpdateUserPresenter {
    @Override
    public void updateIcon(Uri iconUri) {
        final CustomSharedPreferencesManager sharedPreferencesManager=CustomSharedPreferencesManager.getInstance();
        File icon=new File(FileUtil.getFilePathByUri(getmContext(),iconUri));
        RequestBody body=RequestBody.create(MediaType.parse("image/*"),icon);
        if(NetUtil.isNetWorkAvailable()){
            getmModel().updateIcon(
                    sharedPreferencesManager.getUser().getUser_id()
                    , MultipartBody.Part.createFormData("icon", icon.getName(), body)
                    , new ICallbackListener<User>() {
                        @Override
                        public void onSuccess(User data) {
                            sharedPreferencesManager.saveUser(data);
                            DBManager.getInstance().getDaoSession().getUserDao().update(data);
                            getmView().onUpdateSuccess(data);
                        }

                        @Override
                        public void onFailed(String msg) {
                            getmView().onUpdateFailed("上传失败");
                        }
                    });
        }else
            getmView().onUpdateFailed("网络连接不可用");
    }

    @Override
    public void resetPwd(String phone, String pwd) {
        if(NetUtil.isNetWorkAvailable()){
            final User user=new User();
            user.setUser_phone(phone);
            user.setUser_pwd(pwd);
            getmModel().resetPwd(user, new ICallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    getmView().onUpdateSuccess(user);
                }

                @Override
                public void onFailed(String msg) {
                    if(checkAttach())
                        getmView().onUpdateFailed(msg);
                }
            });
        }else
            if(checkAttach())
                getmView().onUpdateFailed("网络连接不可用");
    }

    @Override
    public void updateOther(User user) {
        if(NetUtil.isNetWorkAvailable()){
            getmModel().updateOther(user
                    , new ICallbackListener<User>() {
                        @Override
                        public void onSuccess(User data) {
                            getmView().onUpdateSuccess(data);
                        }

                        @Override
                        public void onFailed(String msg) {
                            getmView().onUpdateFailed("更新失败");
                        }
                    });
        }else
            getmView().onUpdateFailed("网络连接不可用");
    }

    @Override
    public UpdateUserContract.IUpdateUserModel createModel() {
        return new UpdateUserModel();
    }
}
