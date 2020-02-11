package com.lyh.guanbei.bean;


import android.database.Cursor;

import com.lyh.guanbei.db.BookDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private double max_sum;        //限额
    private double now_in_sum;         //现在金额
    private double now_out_sum;         //现在金额
    private int status;         //DB状态
    private double in_sum;      //总收入
    private double out_sum;     //总支出

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

    @Generated(hash = 812964935)
    public Book(Long local_id, @NotNull String book_name, long book_id, long manager_id, String person_id, double max_sum, double now_in_sum,
                double now_out_sum, int status, double in_sum, double out_sum) {
        this.local_id = local_id;
        this.book_name = book_name;
        this.book_id = book_id;
        this.manager_id = manager_id;
        this.person_id = person_id;
        this.max_sum = max_sum;
        this.now_in_sum = now_in_sum;
        this.now_out_sum = now_out_sum;
        this.status = status;
        this.in_sum = in_sum;
        this.out_sum = out_sum;
    }

    @Generated(hash = 1839243756)
    public Book() {
    }

    @Override
    public String toString() {
        return "Book{" +
                "local_id=" + local_id +
                ", book_name='" + book_name + '\'' +
                ", book_id=" + book_id +
                ", manager_id=" + manager_id +
                ", person_id='" + person_id + '\'' +
                ", max_sum=" + max_sum +
                ", now_in_sum=" + now_in_sum +
                ", now_out_sum=" + now_out_sum +
                ", status=" + status +
                ", in_sum=" + in_sum +
                ", out_sum=" + out_sum +
                '}';
    }

    public double getNow_in_sum() {
        return now_in_sum;
    }

    public void setNow_in_sum(double now_in_sum) {
        this.now_in_sum = now_in_sum;
    }

    public double getNow_out_sum() {
        return now_out_sum;
    }

    public void setNow_out_sum(double now_out_sum) {
        this.now_out_sum = now_out_sum;
    }

    public double getMax_sum() {
        return max_sum;
    }

    public void setMax_sum(double max_sum) {
        this.max_sum = max_sum;
    }

    public void setMax_sum(String max_sum) {
        this.max_sum = Double.parseDouble(max_sum);
    }

    public double getIn_sum() {
        return in_sum;
    }

    public void setIn_sum(double in_sum) {
        this.in_sum = in_sum;
    }

    public double getOut_sum() {
        return out_sum;
    }

    public void setOut_sum(double out_sum) {
        this.out_sum = out_sum;
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

    public static List<Book> query(WhereCondition cond, WhereCondition... condMore) {
        return DBManager.getInstance().getDaoSession().getBookDao().queryBuilder().where(cond, condMore).list();
    }

    public static void delete(WhereCondition cond, WhereCondition... condMore) {
        DBManager.getInstance().getDaoSession().getBookDao().deleteInTx(query(cond, condMore));
    }

    public static List<Book> queryByUserId() {
        return query(BookDao.Properties.Local_id.in(Util.getLongFromData(CustomSharedPreferencesManager.getInstance().getUser().getLocal_book_id())));
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

    private static final String QUERYSUMBETWEENDATE = "select  sum(amount) as amount,amount_type from Record" +
            " where book_local_id=? and amount_type= ? and date between ? and ? ";
    private static final String QUERYSUM = "select sum(amount) as amount,amount_type from Record" +
            " where book_local_id=? and amount_type= ? ";
    private static final String COLUMN_NAME = "amount";

    public static void updateBookSum(long localid) {
        Book book = queryByLocalId(localid);
        //当月收入
        book.setNow_in_sum(getResFromSQL(QUERYSUMBETWEENDATE, COLUMN_NAME, new String[]{localid + "", Tag.IN + "", DateUtil.getMonthFirstDay(), DateUtil.getNowDateTimeWithoutSecond()}));
        //当月支出
        book.setNow_out_sum(getResFromSQL(QUERYSUMBETWEENDATE, COLUMN_NAME, new String[]{localid + "", Tag.OUT + "", DateUtil.getMonthFirstDay(), DateUtil.getNowDateTimeWithoutSecond()}));
        //总收入
        book.setIn_sum(getResFromSQL(QUERYSUM, COLUMN_NAME, new String[]{localid + "", Tag.IN + ""}));
        //总支出
        book.setOut_sum(getResFromSQL(QUERYSUM, COLUMN_NAME, new String[]{localid + "", Tag.OUT + ""}));
        DBManager.getInstance().getDaoSession().getBookDao().update(book);
    }

    public static void updateBookSum(List<Record> recordList) {
        for (long id : getBookListAbout(recordList))
            updateBookSum(id);
    }

    private static double getResFromSQL(String sql, String columnName, String[] args) {
        Cursor c = DBManager.getInstance().getDaoSession().getDatabase().rawQuery(sql, args);
        double res = 0;
        if (c.moveToFirst())
            res = c.getDouble(c.getColumnIndex(columnName));
        c.close();
        return res;
    }

    //返回有关的booklist(localId)
    public static Set<Long> getBookListAbout(List<Record> recordList) {
        Set<Long> bookIdList = new HashSet<>();
        for (Record r : recordList) {
            bookIdList.add(r.getBook_local_id());
        }
        return bookIdList;
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
