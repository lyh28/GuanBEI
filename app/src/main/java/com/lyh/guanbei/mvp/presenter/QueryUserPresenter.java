package com.lyh.guanbei.mvp.presenter;

import android.content.Context;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.mvp.contract.QueryUserContract;
import com.lyh.guanbei.mvp.model.QueryUserModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class QueryUserPresenter extends BasePresenter<QueryUserContract.IQueryUserView, QueryUserContract.IQueryUserModel> implements QueryUserContract.IQueryUserPresenter {

    @Override
    public void query(long id) {
        List<Long> list=new ArrayList<>();
        list.add(id);
        query(list);
    }

    @Override
    public void queryServer(long id) {
        List<Long> list=new ArrayList<>();
        list.add(id);
        queryServer(list);
    }

    @Override
    public void query(String phone) {
        getmModel().queryLocal(phone, new ICallbackListener<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                getmView().onQueryUserSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                LogUtil.logD(msg);
            }
        });
        queryServer(phone);
    }

    @Override
    public void query(List<Long> id) {
        getmModel().queryLocal(id, new ICallbackListener<List<UserBean>>() {
            @Override
            public void onSuccess(List<UserBean> data) {
                getmView().onQueryUserSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                LogUtil.logD(msg);
            }
        });
        queryServer(id);
    }

    @Override
    public void queryServer(List<Long> id) {
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().queryServer(id, new ICallbackListener<List<UserBean>>() {
                @Override
                public void onSuccess(List<UserBean> data) {
                    getmView().onQueryUserSuccess(data);
                    getmModel().saveData(data);
                }

                @Override
                public void onFailed(String msg) {
                    LogUtil.logD(msg);
                    getmView().onQueryUserError(msg);
                }
            });
        }
    }

    @Override
    public void queryServer(String phone) {
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().queryServer(phone, new ICallbackListener<UserBean>() {
                @Override
                public void onSuccess(UserBean data) {
                    getmView().onQueryUserSuccess(data);
                    getmModel().saveData(data);
                }

                @Override
                public void onFailed(String msg) {
                    LogUtil.logD(msg);
                    getmView().onQueryUserError(msg);
                }
            });
        }
    }

    @Override
    public QueryUserContract.IQueryUserModel createModel() {
        return new QueryUserModel();
    }
}
