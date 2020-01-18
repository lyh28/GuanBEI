package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.CommitRecordContract;
import com.lyh.guanbei.mvp.model.CommitRecordModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class CommitRecordPresenter extends BasePresenter<CommitRecordContract.ICommitRecordView, CommitRecordContract.ICommitRecordModel> implements CommitRecordContract.ICommitRecordPresenter {
    private List<Record> recordList;

    public CommitRecordPresenter() {
        super();
        recordList = new ArrayList<>();
    }

    @Override
    public void commit() {
        commit(recordList);
    }

    @Override
    public void commit(Record record) {
        add(record);
        commit(recordList);
    }

    @Override
    public void add(Record record) {
        //检测信息
        if (checkText(record))
            recordList.add(record);
    }

    @Override
    public void commit(final List<Record> record) {
        if (record == null || record.size() == 0) {
            if (recordList.size() != 0)
                commit(recordList);
            return;
        }
        getmModel().save(record);
        commitService(record);
    }

    @Override
    public void commitService(final List<Record> record) {
        if (record == null || record.size() == 0) return;
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().commit(record, new ICallbackListener<List<Record>>() {
                @Override
                public void onSuccess(List<Record> data) {
                    for (int i = 0; i < data.size(); i++) {
                        record.get(i).setRecord_id(data.get(i).getRecord_id());
                        record.get(i).setStatus(data.get(i).getStatus());
                    }
                    DBManager.getInstance().getAsyncSession().updateInTx(Record.class, record);
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
    private boolean checkText(Record record) {
        if (TextUtils.isEmpty(record.getDate())) {
            getmView().onMessageError("日期不能为空");
            return false;
        } else if (TextUtils.isEmpty(record.getAmount())) {
            getmView().onMessageError("金额不能为空");
            return false;
        }
        return true;
    }
}
