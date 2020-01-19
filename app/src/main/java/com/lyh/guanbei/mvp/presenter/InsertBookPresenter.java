package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.InsertBookContract;
import com.lyh.guanbei.mvp.model.InsertBookModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;
import com.lyh.guanbei.util.Util;

import java.util.ArrayList;
import java.util.List;

public class InsertBookPresenter extends BasePresenter<InsertBookContract.IInsertBookView, InsertBookContract.IInsertBookModel> implements InsertBookContract.IInsertBookPresenter {
    @Override
    public void insert(Book book) {
        if(TextUtils.isEmpty(book.getBook_name())){
            getmView().onMessageError("账本名不能为空");
            return ;
        }
        List<Book> list=new ArrayList<>();
        list.add(book);
        insert(list);
    }

    @Override
    public void insert(final List<Book> bookList) {
        getmModel().insertLocal(bookList);
        insertBookLocalIdToUser(bookList);
        if (checkAttach())
            getmView().onInsertSuccess();
        insertService(bookList);
    }
    private void insertBookLocalIdToUser(List<Book> bookList){
        //在用户表数据中添加bookId
        User user=CustomSharedPreferencesManager.getInstance().getUser();
        String bookId=user.getLocal_book_id();
        for(Book book:bookList){
            bookId=Util.addToData(book.getLocal_id(),bookId);
        }
        user.setLocal_book_id(bookId);
        CustomSharedPreferencesManager.getInstance().saveUser(user);
        DBManager.getInstance().getDaoSession().getUserDao().update(user);
    }
    private void insertBookIdToUser(List<Book> bookList){
        User user=CustomSharedPreferencesManager.getInstance().getUser();
        String bookId=user.getBook_id();
        for(Book book:bookList){
            bookId=Util.addToData(book.getBook_id(),bookId);
        }
        user.setBook_id(bookId);
        CustomSharedPreferencesManager.getInstance().saveUser(user);
        DBManager.getInstance().getDaoSession().getUserDao().update(user);
    }
    @Override
    public void insertService(final List<Book> bookList) {
        if(bookList==null||bookList.size()==0)  return ;
        if(NetUtil.isNetWorkAvailable()){
            getmModel().insertService(bookList, new ICallbackListener<List<Book>>() {
                @Override
                public void onSuccess(List<Book> data) {
                    for(int i=0;i<data.size();i++){
                        data.get(i).setLocal_id(bookList.get(i).getLocal_id());
                    }
                    DBManager.getInstance().getDaoSession().getBookDao().updateInTx(data);
                    insertBookIdToUser(data);
                }

                @Override
                public void onFailed(String msg) {
                    if (checkAttach())
                        getmView().onMessageError(msg);
                }
            });
        }
    }

    @Override
    public InsertBookContract.IInsertBookModel createModel() {
        return new InsertBookModel();
    }
}
