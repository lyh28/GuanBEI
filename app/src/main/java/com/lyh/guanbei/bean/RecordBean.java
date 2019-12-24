package com.lyh.guanbei.bean;

import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.RecordBeanDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

@Entity
public class RecordBean {
    @Id(autoincrement = true)
    private Long record_id;
    @NotNull
    @Index
    private long user_id;
    @NotNull
    @Index
    private long book_id;
    private String time;
    @NotNull
    private String amount;
    private String payto;
    private String content;
    private String remark;      //备注
    private String category;    //分类
    private int status;         //DB状态
    /*
        状态描述：
        0：  仅客户端拥有，服务端未拥有
        1：  客户端和服务端共同拥有     正常状态
        2：  客户端进行了修改，未同步至服务端
        -1： 删除状态，仅客户端已经删除，需要同步至服务端
     */
    public RecordBean(String time, String amount, String payto, String content, String remark) {
        this.time = time;
        this.amount = amount;
        this.payto = payto;
        this.content = content;
        this.remark = remark;
    }

    public RecordBean(long user_id, long book_id, String time, String amount, String category) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.time = time;
        this.amount = amount;
        this.category = category;
    }

    public RecordBean(long user_id, long book_id, String time, String amount, String payto, String content, String remark, String category) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.time = time;
        this.amount = amount;
        this.payto = payto;
        this.content = content;
        this.remark = remark;
        this.category = category;
    }

    @Generated(hash = 264246353)
    public RecordBean(Long record_id, long user_id, long book_id, String time, @NotNull String amount, String payto, String content,
            String remark, String category, int status) {
        this.record_id = record_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.time = time;
        this.amount = amount;
        this.payto = payto;
        this.content = content;
        this.remark = remark;
        this.category = category;
        this.status = status;
    }

    @Generated(hash = 96196931)
    public RecordBean() {
    }

    public Long getRecord_id() {
        return this.record_id;
    }

    public void setRecord_id(long record_id) {
        this.record_id = record_id;
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getBook_id() {
        return this.book_id;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayto() {
        return this.payto;
    }

    public void setPayto(String payto) {
        this.payto = payto;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static List<RecordBean> query(WhereCondition cond,WhereCondition... condMore){
        return GuanBeiApplication.getDaoSession().getRecordBeanDao().queryBuilder().where(cond,condMore).list();
    }
    @Override
    public String toString() {
        return "RecordBean{" +
                "record_id=" + record_id +
                ", user_id=" + user_id +
                ", book_id=" + book_id +
                ", time='" + time + '\'' +
                ", amount='" + amount + '\'' +
                ", payto='" + payto + '\'' +
                ", content='" + content + '\'' +
                ", remark='" + remark + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                '}';
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setRecord_id(Long record_id) {
        this.record_id = record_id;
    }
}
