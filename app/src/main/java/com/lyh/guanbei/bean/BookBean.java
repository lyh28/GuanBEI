package com.lyh.guanbei.bean;


import android.content.Context;

import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.BookBeanDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.util.LogUtil;
import com.lyh.guanbei.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

@Entity
public class BookBean {
    @Id(autoincrement = true)
    private Long book_id;
    @NotNull
    private String book_name;
    @Index
    private long manager_id;
    private String person_id;   //以-分割
    private String max_sum;        //限额
    private String now_sum;         //现在金额
    private int status;         //DB状态

    @Generated(hash = 1223477755)
    public BookBean(Long book_id, @NotNull String book_name, long manager_id, String person_id, String max_sum,
                    String now_sum, int status) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.manager_id = manager_id;
        this.person_id = person_id;
        this.max_sum = max_sum;
        this.now_sum = now_sum;
        this.status = status;
    }

    @Generated(hash = 269018259)
    public BookBean() {
    }

    public BookBean(String book_name, long manager_id) {
        this.book_name = book_name;
        this.manager_id = manager_id;
    }

    /*
            状态描述：
            0：  仅客户端拥有，服务端未拥有
            1：  客户端和服务端共同拥有     正常状态
            2：  客户端进行了修改，未同步至服务端
            -1： 删除状态，仅客户端已经删除，需要同步至服务端

         */
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

    @Override
    public String toString() {
        return "BookBean{" +
                "book_id=" + book_id +
                ", book_name='" + book_name + '\'' +
                ", manager_id=" + manager_id +
                ", person_id='" + person_id + '\'' +
                ", max_sum='" + max_sum + '\'' +
                ", now_sum='" + now_sum + '\'' +
                ", status=" + status +
                '}';
    }

    public static List<BookBean> query(WhereCondition cond, WhereCondition... condMore) {
        return GuanBeiApplication.getDaoSession().getBookBeanDao().queryBuilder().where(cond, condMore).list();
    }

    public static List<BookBean> queryByUserId( Context context) {
        List<Long> bookIds = Util.getLongFromData((String) CustomSharedPreferencesManager.getInstance(context).getUser().getBook_id());
        return query(BookBeanDao.Properties.Book_id.in(bookIds));
    }

    public static BookBean queryByBookId(long bookId) {
        return GuanBeiApplication.getDaoSession().getBookBeanDao().load(bookId);
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
}
