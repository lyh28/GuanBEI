package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.DeleteBookUserContract;
import com.lyh.guanbei.util.Util;

public class DeleteBookUserModel implements DeleteBookUserContract.IDeleteBookUserModel {
    @Override
    public void deleteBookUserLocal(long userId, long bookId) {
        BookBean bookBean= GuanBeiApplication.getDaoSession().getBookBeanDao().load(bookId);
        String str= Util.deleteFormData(userId,bookBean.getPerson_id());
        bookBean.setPerson_id(str);
        GuanBeiApplication.getDaoSession().getBookBeanDao().update(bookBean);
    }

    @Override
    public void deleteBookUserService(long userId, long bookId, final ICallbackListener<String> iCallbackListener) {
        APIManager.deleteBookUser(userId, bookId, new BaseObscriber<String>() {
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
