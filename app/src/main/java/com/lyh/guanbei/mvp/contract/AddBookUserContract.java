package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.BookBean;

public interface AddBookUserContract {
    interface IAddBookUserView extends IView {
        void onChangeManagerSuccess();
        void onAddUserRequestSuccess();
        void onNoAccount();       //未登录账号时
        void onAddBookUserFailed(String msg);
    }
    interface IAddBookUserPresenter extends IPresenter<IAddBookUserView,IAddBookUserModel>{
        void addUserRequest(long userId,long bookId);
        void changeManager(long newId,long bookId);
    }
    interface IAddBookUserModel extends IModel{
        void addUserRequest(long userId, long requestId, long bookId, ICallbackListener<String> iCallbackListener);
        void changeManager(long oldId,long newId,long bookId,ICallbackListener<BookBean> iCallbackListener);
    }
}

