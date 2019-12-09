package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.DeleteBookUserContract;
import com.lyh.guanbei.mvp.model.DeleteBookUserModel;
import com.lyh.guanbei.util.NetUtil;

public class DeleteBookUserPresenter extends BasePresenter<DeleteBookUserContract.IDeleteBookUserView, DeleteBookUserContract.IDeleteBookUserModel> implements DeleteBookUserContract.IDeleteBookUserPresenter {
    @Override
    public void deleteBookUser(final long userId, final long bookId) {
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().deleteBookUserService(userId, bookId, new ICallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    getmModel().deleteBookUserLocal(userId,bookId);
                    getmView().onDeleteBookUserSuccess(userId);
                }

                @Override
                public void onFailed(String msg) {
                    getmView().onDeleteBookUserFailed(msg);
                }
            });
        }
        else
            getmView().onDeleteBookUserFailed("网络异常");
    }

    @Override
    public DeleteBookUserContract.IDeleteBookUserModel createModel() {
        return new DeleteBookUserModel();
    }
}
