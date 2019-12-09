package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.RecordBean;

import org.greenrobot.greendao.annotation.Id;

import java.util.List;

public interface DeleteRecordContract {
    interface IDeleteRecordView extends IView{
    }
    interface IDeleteRecordPresenter extends IPresenter<IDeleteRecordView,IDeleteRecordModel>{
        void delete(RecordBean record);
        void delete(List<RecordBean> recordList);
        //分情况
        //情况1.一直没有上传服务器的，此时判断commit,如果commit为false，就直接删除本地数据库即可
        //情况2.服务器有记录的，此时先删除本地数据库，有网的话也删除服务器，删除成功的话把删除表中数据删除,失败的话加入到删除表
    }
    interface IDeleteRecordModel extends IModel{
        void deleteService(List<Long> idList,ICallbackListener<String> iCallbackListener);
        void deleteLocal(List<Long> idList);
    }
}
