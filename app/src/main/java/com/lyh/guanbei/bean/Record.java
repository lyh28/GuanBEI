package com.lyh.guanbei.bean;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.util.LogUtil;

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
public class Record implements Parcelable {
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

    public static List<Record> query(WhereCondition cond, WhereCondition... condMore) {
        return DBManager.getInstance().getDaoSession().getRecordDao().queryBuilder().where(cond, condMore).orderDesc(RecordDao.Properties.Date, RecordDao.Properties.Local_id).list();
    }

    public static Record queryByRecordId(long id) {
        List<Record> list = query(RecordDao.Properties.Record_id.eq(id));
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    public static Record queryByLocalId(long id) {
        return DBManager.getInstance().getDaoSession().getRecordDao().load(id);
    }

    public static List<Record> queryByKey(String key, long userId) {
        key = "%" + key + "%";
        return query(RecordDao.Properties.User_id.eq(userId)
                , DBManager.getInstance().getDaoSession().getRecordDao().queryBuilder()
                        .or(RecordDao.Properties.Remark.like(key), RecordDao.Properties.Towho.like(key), RecordDao.Properties.Category.like(key), RecordDao.Properties.Amount.like(key)));
    }

    private static final String FIRSTDATE = "select min(date) as date from record where user_id = ? and book_local_id = ?";
    private static final String UPDATE_BOOKLOCALID = "update record set book_local_id = ? where book_id = ?";
    private static final String DATE_COLUMN = "date";

    public static void updateBookLocalId(String bookLocalId, String bookId) {
        DBManager.getInstance().getDaoSession().getDatabase().execSQL(UPDATE_BOOKLOCALID, new String[]{bookLocalId, bookId});
    }

    public static String getFirstRecordDate(long book_id) {
        long userId = CustomSharedPreferencesManager.getInstance().getUser().getUser_id();
        String[] args = new String[2];
        args[0] = userId + "";
        args[1] = book_id + "";
        return getResFromSQL(FIRSTDATE, DATE_COLUMN, args);
    }

    private static String getResFromSQL(String sql, String columnName, String[] args) {
        Cursor c = DBManager.getInstance().getDaoSession().getDatabase().rawQuery(sql, args);
        String res = "";
        if (c.moveToFirst())
            res = c.getString(c.getColumnIndex(columnName));
        c.close();
        return res;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(local_id);
        dest.writeLong(record_id);
        dest.writeLong(user_id);
        dest.writeLong(book_id);
        dest.writeLong(book_local_id);
        dest.writeString(date);
        dest.writeDouble(amount);
        dest.writeInt(amount_type);
        dest.writeString(towho);
        dest.writeString(remark);
        dest.writeString(category);
        dest.writeInt(status);
    }

    private Record(Parcel data) {
        local_id=data.readLong();
        record_id=data.readLong();
        user_id=data.readLong();
        book_id=data.readLong();
        book_local_id=data.readLong();
        date=data.readString();
        amount=data.readDouble();
        amount_type=data.readInt();
        towho=data.readString();
        remark=data.readString();
        category=data.readString();
        status=data.readInt();
    }

    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}
