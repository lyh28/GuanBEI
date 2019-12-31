package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.QueryRecordContract;

import java.util.List;

import static com.lyh.guanbei.mvp.contract.QueryRecordContract.IQueryRecordPresenter.BOOKID;
import static com.lyh.guanbei.mvp.contract.QueryRecordContract.IQueryRecordPresenter.USERID;

public class QueryRecordModel implements QueryRecordContract.IQueryRecordModel {
    @Override
    public void queryRecordFromServiceById(final String type,final List<Long> ids, final ICallbackListener<List<Record>> iCallbackListener) {
        BaseObscriber baseObscriber=new BaseObscriber<List<Record>>() {
            @Override
            protected void onSuccess(List<Record> data) {
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
    public void queryRecordFromLocalById(String type, List<Long> ids, ICallbackListener<List<Record>> iCallbackListener) {
        List<Record> recordList =null;
        if(BOOKID.equals(type)){
            recordList = Record.query(RecordDao.Properties.Book_local_id.in(ids));
        }else if(USERID.equals(type)){
            recordList = Record.query(RecordDao.Properties.User_id.in(ids));
        }
        if(recordList ==null|| recordList.size()==0)
            iCallbackListener.onFailed("查询失败");
        else
            iCallbackListener.onSuccess(recordList);
    }

}
