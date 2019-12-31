package com.lyh.guanbei.mvp.contract;

import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.base.IPresenter;
import com.lyh.guanbei.base.IView;
import com.lyh.guanbei.bean.Book;

public interface ChangeBookManagerContract {
    interface IChangeBookManagerView extends IView{
        void onChangeManagerSuccess();
        void onNoAccount();       //未登录账号时
    }
    interface IChangeBookManagerPresenter extends IPresenter<IChangeBookManagerView,IChangeBookManagerModel>{
        void changeManager(long newId,long bookId);
    }
    interface IChangeBookManagerModel extends IModel{
        void changeManager(long oldId, long newId, long bookId, ICallbackListener<Book> iCallbackListener);
    }
}
