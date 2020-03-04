package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.RecordRepository;
import com.lyh.guanbei.mvp.contract.InsertRecordContract;
import com.lyh.guanbei.mvp.model.InsertRecordModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class InsertRecordPresenter extends BasePresenter<InsertRecordContract.IInsertRecordView, InsertRecordContract.IInsertRecordModel> implements InsertRecordContract.IInsertRecordPresenter {
    private List<Record> recordList;

    public InsertRecordPresenter() {
        super();
        recordList = new ArrayList<>();
    }

    @Override
    public void insert() {
        insert(recordList);
    }

    @Override
    public void insert(Record record) {
        add(record);
        insert(recordList);
    }

    @Override
    public void add(Record record) {
        //检测信息
        if (checkText(record))
            recordList.add(record);
    }

    public void insert(final List<Record> record) {
        if (record == null || record.size() == 0) {
            if (recordList.size() != 0)
                insert(recordList);
            return;
        }
        getmModel().insertLocal(record);
        Book.updateBookSum(record);
        RecordRepository.getSingleton().addRecord(record);
        insertService(record);
    }

    public void insertService(final List<Record> record) {
        if (record == null || record.size() == 0) return;
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().insertService(record, new ICallbackListener<List<Record>>() {
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
    public InsertRecordModel createModel() {
        return new InsertRecordModel();
    }

    //true 为合格
    private boolean checkText(Record record) {
        if (TextUtils.isEmpty(record.getDate())) {
            getmView().onMessageError("日期不能为空");
            return false;
        } else if (record.getAmount()<=0) {
            getmView().onMessageError("金额不能为空");
            return false;
        }
        return true;
    }
}
