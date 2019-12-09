package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.QueryRecordContract;
import com.lyh.guanbei.mvp.model.QueryRecordModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.lyh.guanbei.mvp.contract.QueryRecordContract.IQueryRecordModel.BOOKID;
import static com.lyh.guanbei.mvp.contract.QueryRecordContract.IQueryRecordModel.USERID;

public class QueryRecordPresenter extends BasePresenter<QueryRecordContract.IQueryRecordView, QueryRecordContract.IQueryRecordModel> implements QueryRecordContract.IQueryRecordPresenter {
    @Override
    public void queryRecordById(String type, long id) {
        List<Long> idList=new ArrayList<>();
        idList.add(id);
        queryRecordById(type,idList);
    }

    @Override
    public void queryRecordById(final String type,final List<Long> ids) {
        getmView().startLoading();
        getmModel().queryRecordFromLocalById(type,ids, new ICallbackListener<List<RecordBean>>() {
            @Override
            public void onSuccess(List<RecordBean> data) {
                getmView().onQueryRecordSuccess(data);
            }

            @Override
            public void onFailed(String msg) {}
        });
        if (NetUtil.isNetWorkAvailable())
            getmModel().queryRecordFromServiceById(type,ids, new ICallbackListener<List<RecordBean>>() {
                @Override
                public void onSuccess(final List<RecordBean> data) {
                    getmView().onQueryRecordSuccess(data);
                    getmView().endLoading();
                    GuanBeiApplication.getDaoSession().getRecordBeanDao().insertOrReplaceInTx(data);
                }
                @Override
                public void onFailed(String msg) {
                    getmView().onQueryRecordFailed(msg);
                    getmView().endLoading();
                }
            });
        else
            getmView().endLoading();
    }
    @Override
    public QueryRecordContract.IQueryRecordModel createModel() {
        return new QueryRecordModel();
    }
}
