package com.lyh.guanbei.mvp.presenter;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.DeleteBookBean;
import com.lyh.guanbei.bean.DeleteRecordBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DeleteBookBeanDao;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.mvp.contract.DeleteBookContract;
import com.lyh.guanbei.mvp.model.DeleteBookModel;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class DeleteBookPresenter extends BasePresenter<DeleteBookContract.IDeleteBookView, DeleteBookContract.IDeleteBookModel> implements DeleteBookContract.IDeleteBookPresenter {
    private DeleteRecordPresenter mDeleteRecordPresenter;
    public DeleteBookPresenter(){
        mDeleteRecordPresenter=new DeleteRecordPresenter();
    }
    @Override
    public DeleteBookModel createModel() {
        return new DeleteBookModel();
    }

    @Override
    public void deleteBook(BookBean book) {
        if(book==null)
            return;
        getmModel().deleteBookLocal(book.getBook_id());
        //删除Record表相关记录
        mDeleteRecordPresenter.delete(RecordBean.query(RecordBeanDao.Properties.Book_id.eq(book.getBook_id())));

        if(!book.getCommit()) return;
        if(NetUtil.isNetWorkAvailable()){
            getmModel().deleteBookService(book.getBook_id());
        }else
            GuanBeiApplication.getDaoSession().getDeleteBookBeanDao().insertOrReplace(DeleteBookBean.createDeleteBookBean(book.getBook_id()));
    }

    @Override
    public void deleteBook(List<BookBean> bookList) {
        final List<Long> idList=new ArrayList<>(bookList.size());
        List<Long> localList=new ArrayList<>(bookList.size());
        for(BookBean bookBean:bookList){
            if(bookBean.getCommit()){
                idList.add(bookBean.getBook_id());
            }
            localList.add(bookBean.getBook_id());
        }
        //删除Record表相关记录
        mDeleteRecordPresenter.delete(RecordBean.query(RecordBeanDao.Properties.Book_id.in(localList)));

        getmModel().deleteBookLocal(localList);
        if(!NetUtil.isNetWorkAvailable())
            GuanBeiApplication.getDaoSession().getDeleteBookBeanDao().insertOrReplaceInTx(DeleteBookBean.createDeleteBookBean(idList));
        else
            getmModel().deleteBookService(idList, new ICallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                }

                @Override
                public void onFailed(String msg) {
                    GuanBeiApplication.getDaoSession().getDeleteBookBeanDao().insertOrReplaceInTx(DeleteBookBean.createDeleteBookBean(idList));
                }
            });
    }

    public void deleteService() {
        List<DeleteBookBean> deleteRecord = GuanBeiApplication.getDaoSession().getDeleteBookBeanDao().loadAll();
        List<Long> idList = new ArrayList<>();
        for (DeleteBookBean deleteBookBean : deleteRecord) {
            idList.add(deleteBookBean.getBook_id());
        }
        if (idList==null||idList.size() == 0) return;
        getmModel().deleteBookService(idList, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                GuanBeiApplication.getDaoSession().getDeleteBookBeanDao().deleteAll();
            }
            @Override
            public void onFailed(String msg) {}
        });
    }

}
