package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.DeleteBook;
import com.lyh.guanbei.bean.Record;
import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.mvp.contract.DeleteBookContract;
import com.lyh.guanbei.mvp.model.DeleteBookModel;
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
    public void deleteBook(Book book) {
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
        getmModel().deleteBookLocal(book.getLocal_id());
        //删除Record表相关记录
        mDeleteRecordPresenter.delete(Record.query(RecordDao.Properties.Book_local_id.eq(book.getLocal_id())));
        getmView().onDeleteSuccess();
        if(DBManager.isClientOnly(book.getStatus())){
            return;
        }
        if(NetUtil.isNetWorkAvailable()){
            getmModel().deleteBookService(book.getBook_id());
        }else
            DBManager.getInstance().getDaoSession().getDeleteBookDao().insertOrReplace(DeleteBook.createDeleteBookBean(book.getBook_id()));
    }

    @Override
    public void deleteBook(List<Book> bookList) {
        final List<Long> idList=new ArrayList<>(bookList.size());
        List<Long> localList=new ArrayList<>(bookList.size());
        for(Book book :bookList){
            if(DBManager.isClientServer(book.getStatus())){
                idList.add(book.getBook_id());
            }
            localList.add(book.getLocal_id());
        }
        //删除Record表相关记录
        mDeleteRecordPresenter.delete(Record.query(RecordDao.Properties.Book_local_id.in(localList)));

        getmModel().deleteBookLocal(localList);
        if(!NetUtil.isNetWorkAvailable())
            DBManager.getInstance().getDaoSession().getDeleteBookDao().insertOrReplaceInTx(DeleteBook.createDeleteBookBean(idList));
        else
            getmModel().deleteBookService(idList, new ICallbackListener<String>() {
                @Override
                public void onSuccess(String data) {
                }

                @Override
                public void onFailed(String msg) {
                    DBManager.getInstance().getDaoSession().getDeleteBookDao().insertOrReplaceInTx(DeleteBook.createDeleteBookBean(idList));
                }
            });
    }

    public void deleteService() {
        List<DeleteBook> deleteRecord = DBManager.getInstance().getDaoSession().getDeleteBookDao().loadAll();
        List<Long> idList = new ArrayList<>();
        for (DeleteBook deleteBook : deleteRecord) {
            idList.add(deleteBook.getBook_id());
        }
        if (idList.size() == 0) return;
        getmModel().deleteBookService(idList, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                DBManager.getInstance().getDaoSession().getDeleteBookDao().deleteAll();
            }
            @Override
            public void onFailed(String msg) {}
        });
    }
}
