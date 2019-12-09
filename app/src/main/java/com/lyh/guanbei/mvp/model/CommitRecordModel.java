package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.CommitRecordContract;
import com.lyh.guanbei.util.LogUtil;

import java.util.List;

public class CommitRecordModel implements CommitRecordContract.ICommitRecordModel {
    @Override
    public void commit(List<RecordBean> record, final ICallbackListener<List<RecordBean>> iCallbackListener) {
        APIManager.commitRecord(record, new BaseObscriber<List<RecordBean>>() {
            @Override
            protected void onSuccess(List<RecordBean> data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void save(List<RecordBean> record) {
        //数据库保存
        GuanBeiApplication.getDaoSession().getRecordBeanDao().insertOrReplaceInTx(record,true);
    }
}
