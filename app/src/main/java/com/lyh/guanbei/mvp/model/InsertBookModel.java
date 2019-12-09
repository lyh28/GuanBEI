package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.InsertBookContract;

import java.util.List;

public class InsertBookModel implements InsertBookContract.IInsertBookModel {
    @Override
    public void insertLocal(List<BookBean> bookList) {
        for (BookBean book : bookList)
            GuanBeiApplication.getDaoSession().getBookBeanDao().insertOrReplace(book);
    }

    @Override
    public void insertService(List<BookBean> bookList, final ICallbackListener<List<BookBean>> iCallbackListener) {
        APIManager.insertBook(bookList, new BaseObscriber<List<BookBean>>() {
            @Override
            protected void onSuccess(List<BookBean> data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
}
