package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;

import java.util.List;

public class UpdateRecordModel implements UpdateRecordContract.IUpdateRecordModel {
    @Override
    public void updateService(Record record, final ICallbackListener<String> callbackListener) {
        APIManager.updateRecord(record, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {
                callbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                callbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void updateService(List<Record> record, final ICallbackListener<String> callbackListener) {
        APIManager.updateRecord(record, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {
                callbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                callbackListener.onFailed(msg);
            }
        });
    }

    @Override
    public void updateLocal(Record record) {
        DBManager.getInstance().getDaoSession().getRecordDao().update(record);
    }

    @Override
    public void updateLocal(List<Record> record) {
        DBManager.getInstance().getDaoSession().getRecordDao().updateInTx(record);
    }
}
