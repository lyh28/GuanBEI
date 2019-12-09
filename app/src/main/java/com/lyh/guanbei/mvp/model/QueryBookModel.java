package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.BookBeanDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.QueryBookContract;

import java.util.List;

public class QueryBookModel implements QueryBookContract.IQueryBookModel {
    @Override
    public void queryBookFormService(List<Long> idList, final ICallbackListener<List<BookBean>> iCallbackListener) {
        APIManager.queryBook(idList, new BaseObscriber<List<BookBean>>() {
            @Override
            protected void onSuccess(List<BookBean> data) {
                iCallbackListener.onSuccess(data);
            }
            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed("查询账本失败 "+msg);
            }
        });
    }

    @Override
    public void queryBookFormLocal(List<Long> idList, ICallbackListener<List<BookBean>> iCallbackListener) {
        List<BookBean> list = BookBean.query(BookBeanDao.Properties.Book_id.in(idList));
        if (list.size() == 0 || list == null)
            iCallbackListener.onFailed("无此记录");
        else
            iCallbackListener.onSuccess(list);
    }
}
