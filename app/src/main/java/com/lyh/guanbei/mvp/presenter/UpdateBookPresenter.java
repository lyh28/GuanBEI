package com.lyh.guanbei.mvp.presenter;

import android.text.TextUtils;

import com.lyh.guanbei.base.BasePresenter;
import com.lyh.guanbei.base.ICallbackListener;
import com.lyh.guanbei.bean.BookBean;
import com.lyh.guanbei.mvp.contract.UpdateBookContract;
import com.lyh.guanbei.mvp.model.UpdateBookModel;
import com.lyh.guanbei.util.NetUtil;

import java.util.ArrayList;
import java.util.List;

public class UpdateBookPresenter extends BasePresenter<UpdateBookContract.IUpdateBookView, UpdateBookContract.IUpdateBookModel> implements UpdateBookContract.IUpdateBookPresenter {
    @Override
    public void updateBook(BookBean book) {
        if(TextUtils.isEmpty(book.getBook_name())){
            getmView().onUpdateBookFailed("账本名不能为空");
            return;
        }
        List<BookBean> list=new ArrayList<>();
        list.add(book);
        updateBook(list);
    }

    @Override
    public void updateBook(final List<BookBean> bookList) {
        //分情况
        //情况1：commit为false的，直接修改本地数据库，有网后当做Commit处理
        //情况2：commit为true的，先修改本地数据库，使change为true，有网的话也修改网上数据库，修改成功则将change修改为false
        if(bookList==null||bookList.size()==0)  return;
        final List<BookBean> serviceList=new ArrayList<>(bookList.size());

        for(BookBean book:bookList){
            if(book.getCommit()){
                book.setChange(true);
                serviceList.add(book);
            }
        }
        getmModel().updateBookLocal(bookList);
        if(NetUtil.isNetWorkAvailable()){
            updateBookService(serviceList);
        }
    }

    @Override
    public void updateBookService(final List<BookBean> bookList) {
        if(bookList==null||bookList.size()==0) return;
        getmModel().updateBookService(bookList, new ICallbackListener<String>() {
            @Override
            public void onSuccess(String data) {
                for(BookBean book:bookList)
                    book.setChange(false);
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
