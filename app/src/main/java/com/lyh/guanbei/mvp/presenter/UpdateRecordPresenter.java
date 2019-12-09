package com.lyh.guanbei.mvp.presenter;

import android.content.Context;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;
import com.lyh.guanbei.mvp.model.UpdateRecordModel;

import java.util.ArrayList;
import java.util.List;

public class UpdateRecordPresenter extends BasePresenter<UpdateRecordContract.IUpdateRecordView,UpdateRecordContract.IUpdateRecordModel> implements UpdateRecordContract.IUpdateRecordPresenter {
    @Override
    public void update(final RecordBean recordBean) {
        if(recordBean.getCommit()){
            recordBean.setChange(true);
            getmModel().updateLocal(recordBean);
            getmModel().updateService(recordBean, new ICallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    recordBean.setChange(false);
                    getmModel().updateLocal(recordBean);
                }
                @Override
                public void onFailed(String msg) {}
            });
        }else{
            getmModel().updateLocal(recordBean);
        }
    }

    @Override
    public void update(List<RecordBean> recordBeans) {
        //分情况
        //情况1：commit为false的，直接修改本地数据库，有网后当做Commit处理
        //情况2：commit为true的，先修改本地数据库，使change为true，有网的话也修改网上数据库，修改成功则将change修改为false
        if(recordBeans==null||recordBeans.size()==0)    return;
        final List<RecordBean> serviceList=new ArrayList<>();

        for(RecordBean recordBean:recordBeans){
            if(recordBean.getCommit()){
                recordBean.setChange(true);
                serviceList.add(recordBean);
            }
        }
        getmModel().updateLocal(recordBeans);
        if(serviceList.size()!=0){
            updateService(serviceList);
        }
    }

    @Override
    public void updateService(final List<RecordBean> recordBeans) {
        if(recordBeans==null||recordBeans.size()==0)  return;
        getmModel().updateService(recordBeans, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                for(RecordBean r:recordBeans)
                    r.setChange(false);
                getmModel().updateLocal(recordBeans);
            }
            @Override
            public void onFailed(String msg) {}
        });
    }

    @Override
    public UpdateRecordContract.IUpdateRecordModel createModel() {
        return new UpdateRecordModel();
    }
}
