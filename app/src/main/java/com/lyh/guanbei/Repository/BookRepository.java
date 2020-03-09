package com.lyh.guanbei.Repository;

import com.lyh.guanbei.bean.Book;
import com.lyh.guanbei.bean.User;
import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.lifecycle.LiveData;

public class BookRepository extends LiveData<Book> {
    private List<Book> books;
    private static BookRepository mSingleton;
    private BookRepository(){
        books=new ArrayList<>();
    }
    public static BookRepository getSingleton(){
        if(mSingleton==null){
            synchronized (BookRepository.class){
                if(mSingleton==null) {
                    mSingleton = new BookRepository();
                }
            }
        }
        return mSingleton;
    }
    public void init(){
        books.clear();
        User user= CustomSharedPreferencesManager.getInstance().getUser();
        List<Long> bookIdList= Util.getLongFromData(user.getLocal_book_id());
        books=Book.query(BookDao.Properties.Local_id.in(bookIdList));
    }
    public void updateBook(Book book){
        for(int i=0;i<books.size();i++){
            if(books.get(i).getLocal_id()==book.getLocal_id()){
                books.set(i,book);
                break;
            }
        }
        postValue(book);
    }
}
