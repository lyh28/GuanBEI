package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
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
        getmModel().queryBookLocal(localIdList, new ICallbackListener<List<Book>>() {
            @Override
            public void onSuccess(List<Book> data) {
                if(data==null||data.size()==0)  queryBookServiceByLocalId(localIdList);
                else if (checkAttach())
                    getmView().queryBookSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                queryBookServiceByLocalId(localIdList);
            }
        });
    }

    @Override
    public void queryBookService(final List<Long> idList) {
        //参数bookid
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().queryBookService(idList, new ICallbackListener<List<Book>>() {
                @Override
                public void onSuccess(List<Book> data) {
                    //删除相应的bookid
                    Book.delete(BookDao.Properties.Book_id.in(idList));
                    //将对应的record的booklocalid改成这个的新id
                    DBManager.getInstance().getDaoSession().getBookDao().insertOrReplaceInTx(data);
                    for(Book book:data){
                        Record.updateBookLocalId(book.getLocal_id()+"",book.getBook_id()+"");
                    }
                    if (checkAttach())
                        getmView().queryBookSuccess(data);
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
    public void queryBookServiceByLocalId(List<Long> localIdList){
        //参数localid
        List<Book> bookList = Book.query(BookDao.Properties.Local_id.in(localIdList));
        List<Long> idlist = new ArrayList<>();
        for (Book b : bookList)
            idlist.add(b.getBook_id());
        queryBookService(idlist);
    }
    public void queryBook(long id) {
        List<Long> list = new ArrayList<>();
        list.add(id);
        queryBook(list);
    }
    public void queryBookService(long id) {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        queryBookService(idList);
    }
    @Override
    public QueryBookContract.IQueryBookModel createModel() {
        return new QueryBookModel();
    }
}
