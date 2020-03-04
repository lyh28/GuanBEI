package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Record;

import java.util.List;

public interface InsertRecordContract {
    interface IInsertRecordView extends IView {
        //填写信息有误
        void onMessageError(String msg);
    }
    interface IInsertRecordPresenter extends IPresenter<InsertRecordContract.IInsertRecordView, InsertRecordContract.IInsertRecordModel> {
        void add(Record record);
        void insert();
        void insert(Record record);
    }
    interface IInsertRecordModel extends IModel {
        //上传服务器
        void insertService(List<Record> record, ICallbackListener<List<Record>> iCallbackListener);
        //保存本地数据库
        void insertLocal(List<Record> record);
    }
}
