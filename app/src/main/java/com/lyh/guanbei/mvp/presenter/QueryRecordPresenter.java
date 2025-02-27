package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.Repository.RecordRepository;
import com.lyh.guanbei.mvp.contract.QueryRecordContract;
import com.lyh.guanbei.mvp.model.QueryRecordModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class QueryRecordPresenter extends BasePresenter<QueryRecordContract.IQueryRecordView, QueryRecordContract.IQueryRecordModel> implements QueryRecordContract.IQueryRecordPresenter {

    public void queryRecordById(String type, long id) {
        List<Long> idList = new ArrayList<>();
        idList.add(id);
        queryRecordById(type, idList);
    }

    @Override
    public void queryRecordById(final String type, final List<Long> ids) {
        getmView().startLoading();
        LogUtil.logD("开始查询");
        getmModel().queryRecordFromLocalById(type, ids, new ICallbackListener<List<Record>>() {
            @Override
            public void onSuccess(List<Record> data) {
                if (checkAttach()) {
                    getmView().onQueryRecordSuccess(data);
                    getmView().endLoading();
                }
            }

            @Override
            public void onFailed(String msg) {
                LogUtil.logD("查询失败");
                queryRecordServiceById(type, ids);
                getmView().endLoading();
            }
        });

    }

    @Override
    public void queryRecordServiceById(String type, final List<Long> ids) {
        //参数 localid
        if (NetUtil.isNetWorkAvailable()) {
            if (type.equals(BOOKID)) {
                final List<Long> idList=new ArrayList<>();
                List<Book> bookList = Book.query(BookDao.Properties.Local_id.in(ids));
                for (Book book : bookList)
                    idList.add(book.getBook_id());
                getmModel().queryRecordFromServiceById(type, idList, new ICallbackListener<List<Record>>() {
                    @Override
                    public void onSuccess(final List<Record> data) {
                        //暂时的处理  全部删除
                        DBManager.getInstance().getDaoSession().getRecordDao().deleteInTx(Record.query(RecordDao.Properties.Book_id.in(idList)));
                        for (Record record : data) {
                            for (int i = 0; i < idList.size(); i++)
                                if (record.getBook_id() == idList.get(i)) {
                                    record.setBook_local_id(ids.get(i));
                                }
                        }
                        DBManager.getInstance().getDaoSession().getRecordDao().insertOrReplaceInTx(data);
                        RecordRepository.getSingleton().init();
                        if (checkAttach()) {
                            getmView().onQueryRecordSuccess(data);
                            getmView().endLoading();
                        }
                    }

                    @Override
                    public void onFailed(String msg) {
                        if (checkAttach()) {
                            getmView().onQueryRecordFailed(msg);
                            getmView().endLoading();
                        }
                    }
                });
            }else{
                getmModel().queryRecordFromServiceById(type, ids, new ICallbackListener<List<Record>>() {
                    @Override
                    public void onSuccess(final List<Record> data) {
                        //暂时的处理  全部删除
//                        DBManager.getInstance().getDaoSession().getRecordDao().deleteInTx(Record.query(RecordDao.Properties.Book_id.in(idList)));
//                        for (Record record : data) {
//                            for (int i = 0; i < idList.size(); i++)
//                                if (record.getBook_id() == idList.get(i))
//                                    record.setBook_local_id(ids.get(i));
//                        }
                        DBManager.getInstance().getDaoSession().getRecordDao().insertOrReplaceInTx(data);
                        RecordRepository.getSingleton().init();
                        if (checkAttach()) {
                            getmView().onQueryRecordSuccess(data);
                            getmView().endLoading();
                        }
                    }
                    @Override
                    public void onFailed(String msg) {
                        if (checkAttach()) {
                            getmView().onQueryRecordFailed(msg);
                            getmView().endLoading();
                        }
                    }
                });
            }
        }
    }

    @Override
    public QueryRecordContract.IQueryRecordModel createModel() {
        return new QueryRecordModel();
    }
}
