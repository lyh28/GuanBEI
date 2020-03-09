package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.Repository.BookRepository;
import com.lyh.guanbei.Repository.DataViewModel;
import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.mvp.contract.UpdateBookContract;
import com.lyh.guanbei.mvp.model.UpdateBookModel;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class UpdateBookPresenter extends BasePresenter<UpdateBookContract.IUpdateBookView, UpdateBookContract.IUpdateBookModel> implements UpdateBookContract.IUpdateBookPresenter {
    @Override
    public void updateBook(Book book) {
        if(TextUtils.isEmpty(book.getBook_name())){
            getmView().onUpdateBookFailed("账本名不能为空");
            return;
        }
        List<Book> list=new ArrayList<>();
        list.add(book);
        updateBook(list);
    }

    @Override
    public void updateBook(final List<Book> bookList) {
        //分情况
        //情况1：commit为false的，直接修改本地数据库，有网后当做Commit处理
        //情况2：commit为true的，先修改本地数据库，使change为true，有网的话也修改网上数据库，修改成功则将change修改为false
        if(bookList==null||bookList.size()==0)  return;
        final List<Book> serviceList=new ArrayList<>(bookList.size());

        for(Book book:bookList){
            BookRepository.getSingleton().updateBook(book);
            if(DBManager.isClientServer(book.getStatus())){
                book.setStatus(DBManager.CLIENT_UPDATE_STATUS);
                serviceList.add(book);
            }
        }
        getmModel().updateBookLocal(bookList);
        if(NetUtil.isNetWorkAvailable()){
            updateBookService(serviceList);
        }
    }

    @Override
    public void updateBookService(final List<Book> bookList) {
        if(bookList==null||bookList.size()==0) return;
        getmModel().updateBookService(bookList, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                for(Book book:bookList) {
                    book.setStatus(DBManager.CLIENT_SERVER_STATUS);
                }
                getmModel().updateBookLocal(bookList);
            }

            @Override
            public void onFailed(String msg) {
            }
        });
    }

    @Override
    public UpdateBookContract.IUpdateBookModel createModel() {
        return new UpdateBookModel();
    }
}
