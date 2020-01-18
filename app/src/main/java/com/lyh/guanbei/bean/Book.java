package com.lyh.guanbei.bean;


import android.content.Context;

import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

@Entity
public class Book {
    @Id(autoincrement = true)
    private Long local_id;
    @NotNull
    private String book_name;
    private long book_id;
    @Index
    private long manager_id;
    private String person_id;   //以-分割
    private String max_sum;        //限额
    private String now_sum;         //现在金额
    private int status;         //DB状态
    private String in_sum;      //总收入
    private String out_sum;     //总支出
    /*
            状态描述：
            0：  仅客户端拥有，服务端未拥有
            1：  客户端和服务端共同拥有     正常状态
            2：  客户端进行了修改，未同步至服务端
            -1： 删除状态，仅客户端已经删除，需要同步至服务端
         */
    public Book(String book_name, long manager_id) {
        this.book_name = book_name;
        this.manager_id = manager_id;
    }




    public Long getBook_id() {
        return this.book_id;
    }

    public void setBook_id(Long book_id) {
        this.book_id = book_id;
    }

    public String getBook_name() {
        return this.book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public long getManager_id() {
        return this.manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }

    public String getPerson_id() {
        return this.person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getMax_sum() {
        return this.max_sum;
    }

    public void setMax_sum(String max_sum) {
        this.max_sum = max_sum;
    }
    public static List<Book> query(WhereCondition cond, WhereCondition... condMore) {
        return DBManager.getInstance().getDaoSession().getBookDao().queryBuilder().where(cond, condMore).list();
    }

    public static List<Book> queryByUserId(Context context) {
        return query(BookDao.Properties.Local_id.in(Util.getLongFromData(CustomSharedPreferencesManager.getInstance(context).getUser().getLocal_book_id())));
    }

    public static Book queryByBookId(long bookId) {
        List<Book> books = query(BookDao.Properties.Book_id.eq(bookId));
        if (books == null || books.size() == 0)
            return null;
        return books.get(0);
    }

    public static Book queryByLocalId(long localId) {
        return DBManager.getInstance().getDaoSession().getBookDao().load(localId);

    }

    public String getNow_sum() {
        return this.now_sum;
    }

    public void setNow_sum(String now_sum) {
        this.now_sum = now_sum;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Long getLocal_id() {
        return this.local_id;
    }


    public void setLocal_id(Long local_id) {
        this.local_id = local_id;
    }


    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }
}
