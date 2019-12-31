package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.QueryBookContract;

import java.util.List;

public class QueryBookModel implements QueryBookContract.IQueryBookModel {
    @Override
    public void queryBookFormService(List<Long> idList, final ICallbackListener<List<Book>> iCallbackListener) {
        APIManager.queryBook(idList, new BaseObscriber<List<Book>>() {
            @Override
            protected void onSuccess(List<Book> data) {
                iCallbackListener.onSuccess(data);
            }
            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed("查询账本失败 "+msg);
            }
        });
    }

    @Override
    public void queryBookFormLocal(List<Long> idList, ICallbackListener<List<Book>> iCallbackListener) {
        List<Book> list = Book.query(BookDao.Properties.Local_id.in(idList));
        if (list.size() == 0 || list == null)
            iCallbackListener.onFailed("无此记录");
        else
            iCallbackListener.onSuccess(list);
    }
}
