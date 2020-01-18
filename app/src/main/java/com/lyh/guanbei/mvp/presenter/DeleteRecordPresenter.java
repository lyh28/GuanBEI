package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.DeleteRecord;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.DeleteRecordContract;
import com.lyh.guanbei.mvp.model.DeleteRecordModel;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class DeleteRecordPresenter extends BasePresenter<DeleteRecordContract.IDeleteRecordView, DeleteRecordContract.IDeleteRecordModel> implements DeleteRecordContract.IDeleteRecordPresenter {

    @Override
    public void delete(Record record) {
        List<Record> recordList = new ArrayList<>();
        recordList.add(record);
        delete(recordList);
    }

    @Override
    public void delete(List<Record> recordList) {
        if (recordList == null || recordList.size() == 0) return;
        final List<Long>  serviceDeleteList = new ArrayList<>();
        List<Long> localList = new ArrayList<>(recordList.size());
        //分情况
        //情况1.一直没有上传服务器的，此时判断commit,如果commit为false，就直接删除本地数据库即可
        //情况2.服务器有记录的，此时先删除本地数据库，有网的话也删除服务器，删除成功的话把删除表中数据删除,失败的话加入到删除表
        for (Record record : recordList) {
            if (DBManager.isClientServer(record.getStatus())) {
                //已上传服务器的情况
                serviceDeleteList.add(record.getRecord_id());
            }
            localList.add(record.getLocal_id());
        }
        getmModel().deleteLocal(localList);
        if (serviceDeleteList.size() != 0) {
            if (!NetUtil.isNetWorkAvailable())
                insertDeleteRecord(serviceDeleteList);
            else
                getmModel().deleteService(serviceDeleteList, new ICallbackListener<String>() {
                    @Override
                    public void onSuccess(String data) {
                    }

                    @Override
                    public void onFailed(String msg) {
                        insertDeleteRecord(serviceDeleteList);
                    }
                });
        }
    }

    public void deleteService() {
        List<DeleteRecord> deleteRecord = DBManager.getInstance().getDaoSession().getDeleteRecordDao().loadAll();
        List<Long> idList = new ArrayList<>();
        for (DeleteRecord deleteRecordBean : deleteRecord) {
            idList.add(deleteRecordBean.getRecord_id());
        }
        if (idList.size() == 0) return;
        getmModel().deleteService(idList, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                DBManager.getInstance().getDaoSession().getDeleteRecordDao().deleteAll();
            }
            @Override
            public void onFailed(String msg) { }
        });
    }

    @Override
    public DeleteRecordContract.IDeleteRecordModel createModel() {
        return new DeleteRecordModel();
    }

    private void insertDeleteRecord(List<Long> idList) {
        //添加进删除表中
        DBManager.getInstance().getDaoSession().getDeleteRecordDao().insertOrReplaceInTx(DeleteRecord.createDeleteRecordBean(idList));
    }
}
