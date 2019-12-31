package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.InsertBookContract;

import java.util.List;

public class InsertBookModel implements InsertBookContract.IInsertBookModel {
    @Override
    public void insertLocal(List<Book> bookList) {
        DBManager.getInstance().getDaoSession().getBookDao().insertOrReplaceInTx(bookList);
    }

    @Override
    public void insertService(List<Book> bookList, final ICallbackListener<List<Book>> iCallbackListener) {
        APIManager.insertBook(bookList, new BaseObscriber<List<Book>>() {
            @Override
            protected void onSuccess(List<Book> data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
}
