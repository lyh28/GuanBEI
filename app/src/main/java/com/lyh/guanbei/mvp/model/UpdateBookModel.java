package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.UpdateBookContract;

import java.util.List;

public class UpdateBookModel implements UpdateBookContract.IUpdateBookModel {
    @Override
    public void updateBookService(List<Book> bookList, final ICallbackListener<String> iCallbackListener) {
        APIManager.updateBook(bookList, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {
                iCallbackListener.onSuccess(data);
            }
            @Override
            protected void onFailed(String msg) {
            }
        });
    }

    @Override
    public void updateBookLocal(List<Book> bookList) {
        for(Book book:bookList)
           DBManager.getInstance().getDaoSession().getBookDao().update(book);
    }
}
