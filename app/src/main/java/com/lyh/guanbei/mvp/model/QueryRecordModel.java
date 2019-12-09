package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.http.BaseResponse;
import com.lyh.guanbei.mvp.contract.QueryRecordContract;

import java.util.List;

public class QueryRecordModel implements QueryRecordContract.IQueryRecordModel {
    @Override
    public void queryRecordFromServiceById(final String type,final List<Long> ids, final ICallbackListener<List<RecordBean>> iCallbackListener) {
        BaseObscriber baseObscriber=new BaseObscriber<List<RecordBean>>() {
            @Override
            protected void onSuccess(List<RecordBean> data) {
                iCallbackListener.onSuccess(data);

            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        };
        if(BOOKID.equals(type)){
            APIManager.queryRecordByBookId(ids,baseObscriber);
        }else if(USERID.equals(type)){
            APIManager.queryRecordByUserId(ids,baseObscriber);
        }
    }

    @Override
    public void queryRecordFromLocalById(String type, List<Long> ids, ICallbackListener<List<RecordBean>> iCallbackListener) {
        List<RecordBean> recordBeanList=null;
        if(BOOKID.equals(type)){
            recordBeanList=RecordBean.query(RecordBeanDao.Properties.Book_id.in(ids));
        }else if(USERID.equals(type)){
            recordBeanList=RecordBean.query(RecordBeanDao.Properties.User_id.in(ids));
        }
        if(recordBeanList==null||recordBeanList.size()==0)
            iCallbackListener.onFailed("查询失败");
        else
            iCallbackListener.onSuccess(recordBeanList);
    }

}
