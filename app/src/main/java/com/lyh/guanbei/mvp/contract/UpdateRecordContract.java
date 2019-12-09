package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.RecordBean;

import java.util.List;

public interface UpdateRecordContract {
    interface IUpdateRecordView extends IView{
        //填写信息有误
        void onMessageError(String msg);
    }
    interface IUpdateRecordPresenter extends IPresenter<IUpdateRecordView,IUpdateRecordModel>{
        void update(RecordBean recordBean);
        void update(List<RecordBean> recordBeans);
        void updateService(List<RecordBean> recordBeans);
        //分情况
        //情况1：commit为false的，直接修改本地数据库，有网后当做Commit处理
        //情况2：commit为true的，先修改本地数据库，使change为true，有网的话也修改网上数据库，修改成功则将change修改为false
    }
    interface IUpdateRecordModel extends IModel{
        void updateService(RecordBean recordBean, ICallbackListener<String> callbackListener);
        void updateService(List<RecordBean> recordBean, ICallbackListener<String> callbackListener);
        void updateLocal(RecordBean recordBean);
        void updateLocal(List<RecordBean> recordBean);
    }
}
