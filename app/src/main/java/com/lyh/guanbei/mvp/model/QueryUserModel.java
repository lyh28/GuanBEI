package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.db.UserDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.QueryUserContract;

import java.util.List;

public class QueryUserModel implements QueryUserContract.IQueryUserModel {
    @Override
    public void queryLocal(List<Long> idList, ICallbackListener<List<User>> iCallbackListener) {
        List<User> userList = DBManager.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.User_id.in(idList)).list();
        if (userList == null || userList.size() == 0)
            iCallbackListener.onFailed("无此用户");
        else
            iCallbackListener.onSuccess(userList);
    }

    @Override
    public void queryServer(List<Long> idList, final ICallbackListener<List<User>> iCallbackListener) {
        APIManager.queryUserById(idList, new BaseObscriber<List<User>>() {
            @Override
            protected void onSuccess(List<User> data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void queryLocal(String phone, ICallbackListener<User> iCallbackListener) {
        List<User> userList = DBManager.getInstance().getDaoSession().getUserDao().queryBuilder().where(UserDao.Properties.User_phone.eq(phone)).list();
        if (userList == null || userList.size() == 0)
            iCallbackListener.onFailed("无此用户");
        else
            iCallbackListener.onSuccess(userList.get(0));
    }

    @Override
    public void queryServer(String phone,final ICallbackListener<User> iCallbackListener) {
        APIManager.queryUserByPhone(phone, new BaseObscriber<User>() {
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
    public void saveData(User user) {
        DBManager.getInstance().getDaoSession().getUserDao().insertOrReplace(user);
    }

    @Override
    public void saveData(List<User> userList) {
        DBManager.getInstance().getDaoSession().getUserDao().insertOrReplaceInTx(userList);
    }
}
