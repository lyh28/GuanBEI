package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.UserBeanDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.QueryUserContract;

import java.util.List;

public class QueryUserModel implements QueryUserContract.IQueryUserModel {
    @Override
    public void queryLocal(List<Long> idList, ICallbackListener<List<UserBean>> iCallbackListener) {
        List<UserBean> userList = GuanBeiApplication.getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.User_id.in(idList)).list();
        if (userList == null || userList.size() == 0)
            iCallbackListener.onFailed("无此用户");
        else
            iCallbackListener.onSuccess(userList);
    }

    @Override
    public void queryServer(List<Long> idList, final ICallbackListener<List<UserBean>> iCallbackListener) {
        APIManager.queryUserById(idList, new BaseObscriber<List<UserBean>>() {
            @Override
            protected void onSuccess(List<UserBean> data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void queryLocal(String phone, ICallbackListener<UserBean> iCallbackListener) {
        List<UserBean> userList = GuanBeiApplication.getDaoSession().getUserBeanDao().queryBuilder().where(UserBeanDao.Properties.User_phone.eq(phone)).list();
        if (userList == null || userList.size() == 0)
            iCallbackListener.onFailed("无此用户");
        else
            iCallbackListener.onSuccess(userList.get(0));
    }

    @Override
    public void queryServer(String phone,final ICallbackListener<UserBean> iCallbackListener) {
        APIManager.queryUserByPhone(phone, new BaseObscriber<UserBean>() {
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

    @Override
    public void saveData(UserBean user) {
        GuanBeiApplication.getDaoSession().getUserBeanDao().insertOrReplace(user);
    }

    @Override
    public void saveData(List<UserBean> userList) {
        GuanBeiApplication.getDaoSession().getUserBeanDao().insertOrReplaceInTx(userList);

    }
}
