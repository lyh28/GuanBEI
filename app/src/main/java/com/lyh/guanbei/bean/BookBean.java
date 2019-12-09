package com.lyh.guanbei.bean;


import com.lyh.guanbei.common.GuanBeiApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
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
    private long manager_id;
    private String person_id;   //以-分割
    private String max_sum;        //限额
    private String now_sum;         //现在金额
    private boolean commit;     //是否上传了
    private boolean change;     //是否更新服务端
    @Generated(hash = 284668111)
    public BookBean(Long book_id, @NotNull String book_name, long manager_id, String person_id, String max_sum,
            String now_sum, boolean commit, boolean change) {
        this.book_id = book_id;
        this.book_name = book_name;
        this.manager_id = manager_id;
        this.person_id = person_id;
        this.max_sum = max_sum;
        this.now_sum = now_sum;
        this.commit = commit;
        this.change = change;
    }
    @Generated(hash = 269018259)
    public BookBean() {
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
    public boolean getCommit() {
        return this.commit;
    }
    public void setCommit(boolean commit) {
        this.commit = commit;
    }
    public boolean getChange() {
        return this.change;
    }
    public void setChange(boolean change) {
        this.change = change;
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
                ", commit=" + commit +
                ", change=" + change +
                '}';
    }

    public static List<BookBean> query(WhereCondition cond, WhereCondition... condMore){
        return GuanBeiApplication.getDaoSession().getBookBeanDao().queryBuilder().where(cond,condMore).list();
    }
    public String getNow_sum() {
        return this.now_sum;
    }
    public void setNow_sum(String now_sum) {
        this.now_sum = now_sum;
    }
}
