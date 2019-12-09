package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;
import com.lyh.guanbei.mvp.model.AddBookUserModel;

public class AddBookUserPresenter extends BasePresenter<AddBookUserContract.IAddBookUserView, AddBookUserContract.IAddBookUserModel> implements AddBookUserContract.IAddBookUserPresenter {
    @Override
    public void addUserRequest(long userId, long bookId) {
        //得到现登录用户的账号
        long requestId = 1;
        if (requestId == -1) {
            getmView().onNoAccount();
            return;
        }
        getmModel().addUserRequest(userId, requestId, bookId, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                getmView().onAddUserRequestSuccess();
            }

            @Override
            public void onFailed(String msg) {
                getmView().onAddBookUserFailed("网络异常");
            }
        });
    }

    @Override
    public void changeManager(long newId, final long bookId) {
        //得到现登录用户的账号
        long oldId = 1;
        if (oldId == -1) {
            getmView().onNoAccount();
            return;
        }
        getmModel().changeManager(oldId, newId, bookId, new ICallbackListener<BookBean>() {
            @Override
            public void onSuccess(BookBean data) {
                getmView().onChangeManagerSuccess();
                updateBook(data);
            }

            @Override
            public void onFailed(String msg) {
                getmView().onAddBookUserFailed("网络异常");
            }
        });
    }

    @Override
    public AddBookUserContract.IAddBookUserModel createModel() {
        return new AddBookUserModel();
    }

    public void updateBook(BookBean bookBean){
        GuanBeiApplication.getDaoSession().getBookBeanDao().update(bookBean);
    }
}
