package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.bean.UserBean;

import java.util.List;

public interface CommitRecordContract {
    interface ICommitRecordView extends IView {
        //填写信息有误
        void onMessageError(String msg);
    }
    interface ICommitRecordPresenter extends IPresenter<CommitRecordContract.ICommitRecordView,CommitRecordContract.ICommitRecordModel> {
        void add(RecordBean record);
        void commit(RecordBean record);
        void commit(List<RecordBean> record);
        void commitService(List<RecordBean> record);
    }
    interface ICommitRecordModel extends IModel {
        //上传服务器
        void commit(List<RecordBean> record, ICallbackListener<List<RecordBean>> iCallbackListener);
        //保存本地数据库
        void save(List<RecordBean> record);
    }
}
