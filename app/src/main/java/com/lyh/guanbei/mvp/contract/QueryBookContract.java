package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.BookBean;

import java.util.List;

public interface QueryBookContract {
    interface IQueryBookView extends IView{
        void showBook(List<BookBean> list);
    }
    interface IQueryBookPresenter extends IPresenter<IQueryBookView,IQueryBookModel>{
        void queryBook(List<Long> idList);
    }
    interface IQueryBookModel extends IModel{
        void queryBookFormService(List<Long> idList, ICallbackListener<List<BookBean>> iCallbackListener);
        void queryBookFormLocal(List<Long> idList, ICallbackListener<List<BookBean>> iCallbackListener);
    }
}
