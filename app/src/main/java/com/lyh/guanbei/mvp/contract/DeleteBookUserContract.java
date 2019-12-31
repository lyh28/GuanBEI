package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;

public interface DeleteBookUserContract {
    interface IDeleteBookUserView extends IView{
        void onDeleteBookUserSuccess(long userId);
        void onDeleteBookUserFailed(String msg);
    }
    interface IDeleteBookUserPresenter extends IPresenter<IDeleteBookUserView,IDeleteBookUserModel>{
        void deleteBookUser(long userId,long localBookId);
    }
    interface IDeleteBookUserModel extends IModel{
        void deleteBookUserLocal(long userId,long localBookId);
        void deleteBookUserService(long userId, long bookId, ICallbackListener<String> iCallbackListener);
    }
}
