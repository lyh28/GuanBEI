package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Book;

import java.util.List;

public interface QueryBookContract {
    interface IQueryBookView extends IView{
        void showBook(List<Book> list);
    }
    interface IQueryBookPresenter extends IPresenter<IQueryBookView,IQueryBookModel>{
        void queryBook(List<Long> idList);
    }
    interface IQueryBookModel extends IModel{
        void queryBookFormService(List<Long> idList, ICallbackListener<List<Book>> iCallbackListener);
        void queryBookFormLocal(List<Long> idList, ICallbackListener<List<Book>> iCallbackListener);
    }
}
