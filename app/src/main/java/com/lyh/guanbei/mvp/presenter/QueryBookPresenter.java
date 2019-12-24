package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.mvp.contract.QueryBookContract;
import com.lyh.guanbei.mvp.model.QueryBookModel;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class QueryBookPresenter extends BasePresenter<QueryBookContract.IQueryBookView, QueryBookContract.IQueryBookModel> implements QueryBookContract.IQueryBookPresenter {
    @Override
    public void queryBook(List<Long> idList) {
        if (idList.size() == 0) return;
        getmModel().queryBookFormLocal(idList, new ICallbackListener<List<BookBean>>() {
            @Override
            public void onSuccess(List<BookBean> data) {
                if (checkAttach())
                    getmView().showBook(data);
            }

            @Override
            public void onFailed(String msg) {
            }
        });
        if (NetUtil.isNetWorkAvailable()) {
            getmModel().queryBookFormService(idList, new ICallbackListener<List<BookBean>>() {
                @Override
                public void onSuccess(List<BookBean> data) {
                    GuanBeiApplication.getDaoSession().getBookBeanDao().insertOrReplaceInTx(data);
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

    @Override
    public QueryBookContract.IQueryBookModel createModel() {
        return new QueryBookModel();
    }
}
