package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.DeleteBook;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.DeleteBookContract;

import java.util.List;

public class DeleteBookModel implements DeleteBookContract.IDeleteBookModel {
    @Override
    public void deleteBookLocal(Long id) {
        DBManager.getInstance().getDaoSession().getBookDao().deleteByKey(id);
    }

    @Override
    public void deleteBookService(final Long id) {
        APIManager.deleteBook(id, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {}

            @Override
            protected void onFailed(String msg) {
                DBManager.getInstance().getDaoSession().getDeleteBookDao().insertOrReplace(DeleteBook.createDeleteBookBean(id));
            }
        });
    }

    @Override
    public void deleteBookLocal(List<Long> idList) {
        DBManager.getInstance().getDaoSession().getBookDao().deleteByKeyInTx(idList);
    }

    @Override
    public void deleteBookService(final List<Long> idList,final ICallbackListener<String> iCallbackListener) {
        APIManager.deleteBookFromList(idList, new BaseObscriber<String>() {
            @Override
            protected void onSuccess(String data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
}
