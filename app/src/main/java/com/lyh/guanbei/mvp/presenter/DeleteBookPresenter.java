package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.base.IModel;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.DeleteBookBean;
import com.lyh.guanbei.bean.DeleteRecordBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.db.DeleteBookBeanDao;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
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
        if(book.getManager_id()!= CustomSharedPreferencesManager.getInstance(getmContext()).getUser().getUser_id()){
            if (checkAttach())
                getmView().onDeleteError("您不是该账本管理员，无权限进行删除");
            return;
        }
        if(!TextUtils.isEmpty(book.getPerson_id())&&!NetUtil.isNetWorkAvailable()){
            if (checkAttach())
                getmView().onDeleteError("该账本为共享账本，需要联网进行删除");
            return;
        }
        getmModel().deleteBookLocal(book.getBook_id());
        //删除Record表相关记录
        mDeleteRecordPresenter.delete(RecordBean.query(RecordBeanDao.Properties.Book_id.eq(book.getBook_id())));
        getmView().onDeleteSuccess();
        if(DBManager.isClientOnly(book.getStatus())){
            return;
        }
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
            if(DBManager.isClientServer(bookBean.getStatus())){
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
