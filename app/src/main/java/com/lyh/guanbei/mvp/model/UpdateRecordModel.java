package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;

import java.util.List;

public class UpdateRecordModel implements UpdateRecordContract.IUpdateRecordModel {
    @Override
    public void updateService(RecordBean recordBean, final ICallbackListener<String> callbackListener) {
        APIManager.updateRecord(recordBean, new BaseObscriber<String>() {
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
    public void updateService(List<RecordBean> recordBean, final ICallbackListener<String> callbackListener) {
        APIManager.updateRecord(recordBean, new BaseObscriber<String>() {
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
    public void updateLocal(RecordBean recordBean) {
        GuanBeiApplication.getDaoSession().getRecordBeanDao().update(recordBean);
    }

    @Override
    public void updateLocal(List<RecordBean> recordBean) {
        GuanBeiApplication.getDaoSession().getRecordBeanDao().updateInTx(recordBean);
    }
}
