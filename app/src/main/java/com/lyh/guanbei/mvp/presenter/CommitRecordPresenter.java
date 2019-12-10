package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.CommitRecordContract;
import com.lyh.guanbei.mvp.model.CommitRecordModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class CommitRecordPresenter extends BasePresenter<CommitRecordContract.ICommitRecordView, CommitRecordContract.ICommitRecordModel> implements CommitRecordContract.ICommitRecordPresenter {
    private List<RecordBean> recordBeanList;

    public CommitRecordPresenter() {
        super();
        recordBeanList = new ArrayList<>();
    }

    @Override
    public void commit(RecordBean record) {
        add(record);
        commit(recordBeanList);
    }

    @Override
    public void add(RecordBean record) {
        //检测信息
        if (checkText(record))
            recordBeanList.add(record);
    }

    @Override
    public void commit(final List<RecordBean> record) {
        if ((record == null || record.size() == 0) && recordBeanList.size() != 0) {
            commit(recordBeanList);
            return;
        }
        getmModel().save(record);
        commitService(record);
    }

    @Override
    public void commitService(final List<RecordBean> record) {
        if (record == null || record.size() == 0) return;
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().commit(record, new ICallbackListener<List<RecordBean>>() {
                @Override
                public void onSuccess(List<RecordBean> data) {
                    GuanBeiApplication.getDaoSession().getRecordBeanDao().deleteInTx(record);
                    getmModel().save(data);
                }

                @Override
                public void onFailed(String msg) {
                    LogUtil.logD("出错" + msg);
                }
            });
        }
    }

    @Override
    public CommitRecordModel createModel() {
        return new CommitRecordModel();
    }

    //true 为合格
    private boolean checkText(RecordBean record) {
        if (TextUtils.isEmpty(record.getTime())) {
            getmView().onMessageError("日期不能为空");
            return false;
        } else if (TextUtils.isEmpty(record.getAmount())) {
            getmView().onMessageError("金额不能为空");
            return false;
        }
        return true;
    }
}
