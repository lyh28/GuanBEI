package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Record;

import java.util.List;

public interface CommitRecordContract {
    interface ICommitRecordView extends IView {
        //填写信息有误
        void onMessageError(String msg);
    }
    interface ICommitRecordPresenter extends IPresenter<CommitRecordContract.ICommitRecordView,CommitRecordContract.ICommitRecordModel> {
        void add(Record record);
        void commit(Record record);
        void commit(List<Record> record);
        void commitService(List<Record> record);
    }
    interface ICommitRecordModel extends IModel {
        //上传服务器
        void commit(List<Record> record, ICallbackListener<List<Record>> iCallbackListener);
        //保存本地数据库
        void save(List<Record> record);
    }
}
