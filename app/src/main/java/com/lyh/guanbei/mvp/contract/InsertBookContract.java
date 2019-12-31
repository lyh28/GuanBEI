package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Book;

import java.util.List;

public interface InsertBookContract {
    interface IInsertBookView extends IView{
        void onMessageError(String msg);
        void onInsertSuccess();
    }
    interface IInsertBookPresenter extends IPresenter<IInsertBookView,IInsertBookModel>{
        void insert(Book book);
        void insert(List<Book> bookList);
        void insertService(List<Book> bookList);
    }
    interface IInsertBookModel extends IModel{
        void insertLocal(List<Book> bookList);
        void insertService(List<Book> bookList, ICallbackListener<List<Book>> iCallbackListener);
    }
}
