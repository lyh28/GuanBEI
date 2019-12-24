package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.BookBean;

import java.util.List;

public interface InsertBookContract {
    interface IInsertBookView extends IView{
        void onMessageError(String msg);
        void onInsertSuccess();
    }
    interface IInsertBookPresenter extends IPresenter<IInsertBookView,IInsertBookModel>{
        void insert(BookBean book);
        void insert(List<BookBean> bookList);
        void insertService(List<BookBean> bookList);
    }
    interface IInsertBookModel extends IModel{
        void insertLocal(List<BookBean> bookList);
        void insertService(List<BookBean> bookList, ICallbackListener<List<BookBean>> iCallbackListener);
    }
}
