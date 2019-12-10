package com.lyh.guanbei.common;

import android.app.IntentService;
import android.content.Intent;

import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.bean.RecordBean;
import com.lyh.guanbei.db.BookBeanDao;
import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.db.DaoSession;
import com.lyh.guanbei.db.RecordBeanDao;
import com.lyh.guanbei.mvp.presenter.CommitRecordPresenter;
import com.lyh.guanbei.mvp.presenter.DeleteBookPresenter;
import com.lyh.guanbei.mvp.presenter.DeleteRecordPresenter;
import com.lyh.guanbei.mvp.presenter.InsertBookPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateBookPresenter;
import com.lyh.guanbei.mvp.presenter.UpdateRecordPresenter;

import java.util.List;

public class NetRestartService extends IntentService{
    //账单
    private CommitRecordPresenter mCommitRecordPresenter;
    private DeleteRecordPresenter mDeleteRecordPresenter;
    private UpdateRecordPresenter mUpdateRecordPresenter;

    //账本
    private InsertBookPresenter mInsertBookPresenter;
    private DeleteBookPresenter mDeleteBookPresenter;
    private UpdateBookPresenter mUpdateBookPresenter;
    public NetRestartService(){
        super("NetRestartService");
        mDeleteRecordPresenter=new DeleteRecordPresenter();
        mDeleteBookPresenter=new DeleteBookPresenter();
    }

    @Override
    public void onStart( Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent( Intent intent) {
        //Book表操作
        //取出需要提交到服务器的
        List<BookBean> insertBook=BookBean.query(BookBeanDao.Properties.Status.eq(DBManager.CLIENT_ONLY_STATUS));
        if(insertBook!=null&&insertBook.size()!=0){
            mInsertBookPresenter=new InsertBookPresenter();
            mInsertBookPresenter.insertService(insertBook);
        }
        //取出需要更新到服务器的
        List<BookBean> updateBook=BookBean.query(BookBeanDao.Properties.Status.eq(DBManager.CLIENT_UPDATE_STATUS));
        if(updateBook!=null&&updateBook.size()!=0){
            mUpdateBookPresenter=new UpdateBookPresenter();
            mUpdateBookPresenter.updateBookService(updateBook);
        }
        //取出并删除需要删除的
        mDeleteBookPresenter.deleteService();

        //Record表操作
        //取出需要提交到服务器的
        List<RecordBean> commitRecord=RecordBean.query(RecordBeanDao.Properties.Status.eq(DBManager.CLIENT_ONLY_STATUS));
        if(commitRecord!=null&&commitRecord.size()!=0){
            mCommitRecordPresenter=new CommitRecordPresenter();
            mCommitRecordPresenter.commitService(commitRecord);
        }
        //取出需要更新到服务器的
        List<RecordBean> updateRecord=RecordBean.query(RecordBeanDao.Properties.Status.eq(DBManager.CLIENT_UPDATE_STATUS));
        if(updateRecord!=null&&updateRecord.size()!=0){
            mUpdateRecordPresenter=new UpdateRecordPresenter();
            mUpdateRecordPresenter.updateService(updateRecord);
        }
        //取出并删除需要删除的
        mDeleteRecordPresenter.deleteService();
    }
}
