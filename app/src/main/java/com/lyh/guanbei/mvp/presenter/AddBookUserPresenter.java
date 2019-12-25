package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.UserBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;
import com.lyh.guanbei.mvp.model.AddBookUserModel;
import com.lyh.guanbei.util.NetUtil;
import com.lyh.guanbei.util.Util;

public class AddBookUserPresenter extends BasePresenter<AddBookUserContract.IAddBookUserView, AddBookUserContract.IAddBookUserModel> implements AddBookUserContract.IAddBookUserPresenter {
    @Override
    public void addUserRequest(long userId, long bookId) {
        //得到现登录用户的账号
        UserBean userBean=CustomSharedPreferencesManager.getInstance(getmContext()).getUser();
        if(userBean==null){
            getmView().onNoAccount();
            return ;
        }
        long requestId = CustomSharedPreferencesManager.getInstance(getmContext()).getUser().getUser_id();
        if (requestId == -1) {
            getmView().onNoAccount();
            return;
        }
        if (!NetUtil.isNetWorkAvailable())
            getmView().onAddBookUserRequestFailed("网络异常");
        else
            getmModel().addUserRequest(userId, requestId, bookId, new ICallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                    if (checkAttach())
                        getmView().onAddBookUserRequestSuccess();
                }

                @Override
                public void onFailed(String msg) {
                    if (checkAttach())
                        getmView().onAddBookUserRequestFailed("网络异常");
                }
            });
    }

    @Override
    public void addUser (final long bookId) {
        UserBean userBean=CustomSharedPreferencesManager.getInstance(getmContext()).getUser();
        if (!NetUtil.isNetWorkAvailable())
            getmView().onAddBookUserRequestFailed("网络异常");
        else
            getmModel().addUser(userBean.getUser_id(), bookId, new ICallbackListener<BookBean>() {
                @Override
                public void onSuccess(BookBean data) {
                    CustomSharedPreferencesManager customSharedPreferencesManager=CustomSharedPreferencesManager.getInstance(getmContext());
                    if(customSharedPreferencesManager.getCurrBookId()==-1)
                        customSharedPreferencesManager.saveCurrBookId(bookId);
                    //更新本地数据库
                    GuanBeiApplication.getDaoSession().getBookBeanDao().insertOrReplace(data);
                    UserBean user=customSharedPreferencesManager.getUser();
                    user.setBook_id(Util.addToData(bookId,user.getBook_id()));
                    customSharedPreferencesManager.saveUser(user);
                    GuanBeiApplication.getDaoSession().getUserBeanDao().insertOrReplace(user);
                    if (checkAttach())
                        getmView().onAddBookUserSuccess();
                }

                @Override
                public void onFailed(String msg) {
                    if (checkAttach())
                        getmView().onAddBookUserFailed("网络异常");
                }
            });
    }
//    @Override
//    public void changeManager(long newId, final long bookId) {
//        //得到现登录用户的账号
//        long oldId = 1;
//        if (oldId == -1) {
//            getmView().onNoAccount();
//            return;
//        }
//        if (!NetUtil.isNetWorkAvailable())
//            getmView().onAddBookUserFailed("网络异常");
//        else
//            getmModel().changeManager(oldId, newId, bookId, new ICallbackListener<BookBean>() {
//                @Override
//                public void onSuccess(BookBean data) {
//                    if (checkAttach())
//                        getmView().onChangeManagerSuccess();
//                    updateBook(data);
//                }
//
//                @Override
//                public void onFailed(String msg) {
//                    if (checkAttach())
//                        getmView().onAddBookUserFailed("网络异常");
//                }
//            });
//    }

    @Override
    public AddBookUserContract.IAddBookUserModel createModel() {
        return new AddBookUserModel();
    }
}
