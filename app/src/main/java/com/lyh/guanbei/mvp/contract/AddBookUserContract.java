package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Book;

public interface AddBookUserContract {
    interface IAddBookUserView extends IView {
        void onAddBookUserRequestSuccess();
        void onNoAccount();       //未登录账号时
        void onAddBookUserRequestFailed(String msg);
        void onAddBookUserSuccess();
        void onAddBookUserFailed(String msg);
    }
    interface IAddBookUserPresenter extends IPresenter<IAddBookUserView,IAddBookUserModel>{
        void addUserRequest(long userId,long bookId);
        void addUser(long bookId);
    }
    interface IAddBookUserModel extends IModel{
        void addUserRequest(long userId, long requestId, long bookId, ICallbackListener<String> iCallbackListener);
        void addUser(long userId,long bookId,ICallbackListener<Book> iCallbackListener);
    }
}

