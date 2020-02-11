package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.QueryBookContract;
import com.lyh.guanbei.mvp.model.QueryBookModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;
import com.lyh.guanbei.util.Util;

import java.util.ArrayList;
import java.util.List;

public class QueryBookPresenter extends BasePresenter<QueryBookContract.IQueryBookView, QueryBookContract.IQueryBookModel> implements QueryBookContract.IQueryBookPresenter {
    @Override
    public void queryBook(final List<Long> localIdList) {
        //参数为local_id
        if (localIdList.size() == 0) return;
        getmModel().queryBookFormLocal(localIdList, new ICallbackListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> data) {
                if (checkAttach())
                    getmView().showBook(data);
            }

            @Override
            public void onFailed(String msg) {
            }
        });
        if (NetUtil.isNetWorkAvailable()) {
            List<Book> bookList = Book.query(BookDao.Properties.Local_id.in(localIdList));
            List<Long> idlist = new ArrayList<>();
            for (Book b : bookList)
                idlist.add(b.getBook_id());
            getmModel().queryBookFormService(idlist, new ICallbackListener<List<Book>>() {
                @Override
                public void onSuccess(List<Book> data) {
                    for (int i = 0; i < data.size(); i++) {
                        data.get(i).setLocal_id(localIdList.get(i));
                    }
                    DBManager.getInstance().getDaoSession().getBookDao().updateInTx(data);
                    if (checkAttach())
                        getmView().showBook(data);
                }

                @Override
                public void onFailed(String msg) {
                }
            });
        }
    }

    public void queryBook(long id) {
        List<Long> list = new ArrayList<>();
        list.add(id);
        queryBook(list);
    }

    public void queryBookServer(long id) {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        queryBookServer(idList);
    }

    public void queryBookServer(final List<Long> idList) {
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().queryBookFormService(idList, new ICallbackListener<List<Book>>() {
                @Override
                public void onSuccess(List<Book> data) {
                    //删除相应的bookid
                    Book.delete(BookDao.Properties.Book_id.in(idList));
                    DBManager.getInstance().getDaoSession().getBookDao().insertOrReplaceInTx(data);
                    if (checkAttach())
                        getmView().showBook(data);
                }

                @Override
                public void onFailed(String msg) {
                    if (checkAttach())
                        getmView().queryBookFailed();
                }
            });
        } else if (checkAttach())
            getmView().queryBookFailed();
    }



    @Override
    public QueryBookContract.IQueryBookModel createModel() {
        return new QueryBookModel();
    }
}
