package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.AddBookUserContract;
import com.lyh.guanbei.mvp.model.AddBookUserModel;
import com.lyh.guanbei.util.NetUtil;
import com.lyh.guanbei.util.Util;

public class AddBookUserPresenter extends BasePresenter<AddBookUserContract.IAddBookUserView, AddBookUserContract.IAddBookUserModel> implements AddBookUserContract.IAddBookUserPresenter {
    @Override
    public void addUserRequest(long userId, long bookId) {
        Book book=Book.queryByLocalId(bookId);
        //得到现登录用户的账号
        User user =CustomSharedPreferencesManager.getInstance(getmContext()).getUser();
        if(user ==null){
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
            getmModel().addUserRequest(userId, requestId, book.getBook_id(), new ICallbackListener<String>() {
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
        //参数bookId为同步ID
        User userBean=CustomSharedPreferencesManager.getInstance(getmContext()).getUser();
        if (!NetUtil.isNetWorkAvailable())
            getmView().onAddBookUserRequestFailed("网络异常");
        else
            getmModel().addUser(userBean.getUser_id(), bookId, new ICallbackListener<Book>() {
                @Override
                public void onSuccess(Book data) {
                    CustomSharedPreferencesManager customSharedPreferencesManager=CustomSharedPreferencesManager.getInstance(getmContext());
                    if(customSharedPreferencesManager.getCurrBookId()==-1)
                        customSharedPreferencesManager.saveCurrBookId(bookId);
                    //获取本地数据库中对应的book
                    Book book=Book.queryByBookId(bookId);
                    data.setLocal_id(book.getLocal_id());
                    //更新本地数据库
                    DBManager.getInstance().getDaoSession().getBookDao().insertOrReplace(data);
                    //更新User
                    User user=customSharedPreferencesManager.getUser();
                    user.setBook_id(Util.addToData(bookId,user.getBook_id()));
                    user.setLocal_book_id(Util.addToData(book.getLocal_id(),user.getLocal_book_id()));
                    customSharedPreferencesManager.saveUser(user);
                    DBManager.getInstance().getDaoSession().getUserDao().insertOrReplace(user);
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
//            getmModel().changeManager(oldId, newId, bookId, new ICallbackListener<Book>() {
//                @Override
//                public void onSuccess(Book data) {
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
