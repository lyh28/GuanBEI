package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.CommitRecordContract;

import java.util.List;

public class CommitRecordModel implements CommitRecordContract.ICommitRecordModel {
    @Override
    public void commit(List<Record> record, final ICallbackListener<List<Record>> iCallbackListener) {
        APIManager.commitRecord(record, new BaseObscriber<List<Record>>() {
            @Override
            protected void onSuccess(List<Record> data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void save(List<Record> record) {
        //数据库保存
        DBManager.getInstance().getDaoSession().getRecordDao().insertOrReplaceInTx(record,true);
    }
}
