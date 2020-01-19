package com.lyh.guanbei.bean;

import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.db.RecordDao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.Serializable;
import java.util.List;

@Entity
public class Record implements Serializable {
    @Transient
    public static final long serialVersionUID=22222L;
    @Id(autoincrement = true)
    private Long local_id;
    @Index
    private long record_id;
    @NotNull
    @Index
    private long user_id;
    private long book_id;
    @Index
    private long book_local_id;
    private String date;
    @NotNull
    private double amount;
    private int amount_type;     //收入还是支出   1 收入    2  支出
    private String towho;
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

    public Record(long user_id, long book_id, long book_local_id, String date, String amount, int amount_type, String towho, String remark, String category) {
        this.user_id = user_id;
        this.book_id = book_id;
        this.book_local_id = book_local_id;
        this.date = date;
        this.amount = Double.parseDouble(amount);
        this.amount_type = amount_type;
        this.towho = towho;
        this.remark = remark;
        this.category = category;
    }


    @Generated(hash = 1513510675)
    public Record(Long local_id, long record_id, long user_id, long book_id, long book_local_id, String date, double amount, int amount_type, String towho,
            String remark, String category, int status) {
        this.local_id = local_id;
        this.record_id = record_id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.book_local_id = book_local_id;
        this.date = date;
        this.amount = amount;
        this.amount_type = amount_type;
        this.towho = towho;
        this.remark = remark;
        this.category = category;
        this.status = status;
    }


    @Generated(hash = 477726293)
    public Record() {
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


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setAmount(String amount) {
        this.amount = Double.parseDouble(amount);
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

    public static List<Record> query(WhereCondition cond, WhereCondition... condMore){
        return DBManager.getInstance().getDaoSession().getRecordDao().queryBuilder().where(cond,condMore).orderDesc(RecordDao.Properties.Date).list();
    }
    public static Record queryByRecordId(long id){
        List<Record> list=query(RecordDao.Properties.Record_id.eq(id));
        if(list==null||list.size()==0)
            return null;
        return list.get(0);
    }
    public static Record queryByLocalId(long id){
        return DBManager.getInstance().getDaoSession().getRecordDao().load(id);
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

    public long getBook_local_id() {
        return this.book_local_id;
    }

    public void setBook_local_id(long book_local_id) {
        this.book_local_id = book_local_id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAmount_type() {
        return this.amount_type;
    }

    public void setAmount_type(int amount_type) {
        this.amount_type = amount_type;
    }

    public String getTowho() {
        return this.towho;
    }

    public void setTowho(String towho) {
        this.towho = towho;
    }

    @Override
    public String toString() {
        return "Record{" +
                "local_id=" + local_id +
                ", record_id=" + record_id +
                ", user_id=" + user_id +
                ", book_id=" + book_id +
                ", book_local_id=" + book_local_id +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", amount_type=" + amount_type +
                ", towho='" + towho + '\'' +
                ", remark='" + remark + '\'' +
                ", category='" + category + '\'' +
                ", status=" + status +
                '}';
    }
}
