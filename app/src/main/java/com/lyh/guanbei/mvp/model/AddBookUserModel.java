package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.http.APIManager;
import com.lyh.guanbei.http.BaseObscriber;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;

public class AddBookUserModel implements AddBookUserContract.IAddBookUserModel {
    @Override
    public void addUserRequest(long userId, long requestId, long bookId, final ICallbackListener<String> iCallbackListener) {
        APIManager.addUserRequest(userId, requestId, bookId, new BaseObscriber<String>() {
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

    @Override
    public void addUser(long userId, long bookId, final ICallbackListener<BookBean> iCallbackListener) {
        APIManager.addBookUser(userId, bookId, new BaseObscriber<BookBean>() {
            @Override
            protected void onSuccess(BookBean data) {
                iCallbackListener.onSuccess(data);
            }

            @Override
            protected void onFailed(String msg) {
                iCallbackListener.onFailed(msg);
            }
        });
    }
    //
//    @Override
//    public void changeManager(long oldId, long newId, long bookId, final ICallbackListener<BookBean> iCallbackListener) {
//        APIManager.changeManager(oldId, newId, bookId, new BaseObscriber<BookBean>() {
//            @Override
//            protected void onSuccess(BookBean data) {
//                iCallbackListener.onSuccess(data);
//            }
//
//            @Override
//            protected void onFailed(String msg) {
//                iCallbackListener.onFailed(msg);
//            }
//        });
//    }


}
