package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Book;

import java.util.List;

public interface UpdateBookContract {
    interface IUpdateBookView extends IView{
        void onUpdateBookFailed(String msg);
    }
    interface IUpdateBookPresenter extends IPresenter<IUpdateBookView,IUpdateBookModel>{
        void updateBook(Book book);
        void updateBook(List<Book> bookList);
        void updateBookService(List<Book> bookList);
    }
    interface IUpdateBookModel extends IModel{
        void updateBookLocal(List<Book> bookList);
        void updateBookService(List<Book> bookList, ICallbackListener<String> iCallbackListener);
    }
}
