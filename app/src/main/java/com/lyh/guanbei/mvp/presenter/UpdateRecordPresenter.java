package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.UpdateRecordContract;
import com.lyh.guanbei.mvp.model.UpdateRecordModel;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class UpdateRecordPresenter extends BasePresenter<UpdateRecordContract.IUpdateRecordView, UpdateRecordContract.IUpdateRecordModel> implements UpdateRecordContract.IUpdateRecordPresenter {
    @Override
    public void update(final Record record) {
        if (DBManager.isClientServer(record.getStatus())) {
            record.setStatus(DBManager.CLIENT_UPDATE_STATUS);
            getmModel().updateLocal(record);
            Book.updateBookSum(record.getBook_local_id());
            if (NetUtil.isNetWorkAvailable())
                getmModel().updateService(record, new ICallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                        record.setStatus(DBManager.CLIENT_SERVER_STATUS);
                        getmModel().updateLocal(record);
                    }

                    @Override
                    public void onFailed(String msg) {
                    }
                });
        } else {
            getmModel().updateLocal(record);
            Book.updateBookSum(record.getBook_local_id());
        }
    }

    @Override
    public void update(List<Record> records) {
        //分情况
        //情况1：commit为false的，直接修改本地数据库，有网后当做Commit处理
        //情况2：commit为true的，先修改本地数据库，使change为true，有网的话也修改网上数据库，修改成功则将change修改为false
        if (records == null || records.size() == 0) return;
        final List<Record> serviceList = new ArrayList<>();

        for (Record record : records) {
            if (DBManager.isClientServer(record.getStatus())) {
                record.setStatus(DBManager.CLIENT_UPDATE_STATUS);
                serviceList.add(record);
            }
        }
        getmModel().updateLocal(records);
        Book.updateBookSum(records);
        if (serviceList.size() != 0&& NetUtil.isNetWorkAvailable()) {
            updateService(serviceList);
        }
    }

    @Override
    public void updateService(final List<Record> records) {
        if (records == null || records.size() == 0) return;
        getmModel().updateService(records, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                for (Record r : records)
                    r.setStatus(DBManager.CLIENT_SERVER_STATUS);
                getmModel().updateLocal(records);
            }

            @Override
            public void onFailed(String msg) {
            }
        });
    }

    @Override
    public UpdateRecordContract.IUpdateRecordModel createModel() {
        return new UpdateRecordModel();
    }
}
