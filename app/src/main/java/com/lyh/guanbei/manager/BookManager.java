package com.lyh.guanbei.manager;

import com.lyh.guanbei.bean.Book;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookManager {
    private static BookManager singleton;
    private List<Book> mBookList;

    private BookManager() {
        mBookList = new ArrayList<>();
        //如果第一次登陆的话就查询服务端   需要设置强制退出
    }

    public void addBook(Book book) {
        mBookList.add(book);
    }
    public void addBook(Collection<Book> c){
        mBookList.addAll(c);
    }
    public Book getBookById(long localId){
        for(Book book:mBookList){

        }
        return null;
    }
    public static BookManager getInstance() {
        if (singleton == null) {
            synchronized (BookManager.class) {
                if (singleton == null)
                    singleton = new BookManager();
            }
        }
        return singleton;
    }
}
