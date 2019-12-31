package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Book;

import java.util.List;

public interface DeleteBookContract {
    interface IDeleteBookView extends IView{
        void onDeleteError(String msg);
        void onDeleteSuccess();
    }
    interface IDeleteBookPresenter extends IPresenter<IDeleteBookView,IDeleteBookModel>{
        void deleteBook(Book book);
        void deleteBook(List<Book> bookList);
    }
    interface IDeleteBookModel extends IModel{
        void deleteBookLocal(Long id);
        void deleteBookService(Long id);
        void deleteBookLocal(List<Long> idList);
        void deleteBookService(List<Long> idList,ICallbackListener<String> iCallbackListener);
    }
}
