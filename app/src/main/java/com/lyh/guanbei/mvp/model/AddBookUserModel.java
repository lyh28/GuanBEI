package com.lyh.guanbei.mvp.model;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
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
    public void addUser(long userId, long bookId, final ICallbackListener<Book> iCallbackListener) {
        APIManager.addBookUser(userId, bookId, new BaseObscriber<Book>() {
            @Override
            protected void onSuccess(Book data) {
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
//    public void changeManager(long oldId, long newId, long bookId, final ICallbackListener<Book> iCallbackListener) {
//        APIManager.changeManager(oldId, newId, bookId, new BaseObscriber<Book>() {
//            @Override
//            protected void onSuccess(Book data) {
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
