package com.lyh.guanbei.bean;

import com.lyh.guanbei.common.Contact;
import com.lyh.guanbei.db.SettingDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;
import com.lyh.guanbei.util.DateUtil;
import com.lyh.guanbei.util.Util;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.Serializable;
import java.util.List;

@Entity
public class User implements Serializable {
    @Transient
    public static final long serialVersionUID = 11111L;
    @Id(autoincrement = true)
    private long user_id;
    private String user_name;
    private String user_icon;
    @Unique
    @NotNull
    private String user_phone;
    private String create_time;
    private String user_pwd;
    private String last_login_time;
    private String book_id;     //以-分割
    private String local_book_id;

    @Generated(hash = 2090104132)
    public User(long user_id, String user_name, String user_icon,
                @NotNull String user_phone, String create_time, String user_pwd,
                String last_login_time, String book_id, String local_book_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_icon = user_icon;
        this.user_phone = user_phone;
        this.create_time = create_time;
        this.user_pwd = user_pwd;
        this.last_login_time = last_login_time;
        this.book_id = book_id;
        this.local_book_id = local_book_id;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", create_time='" + create_time + '\'' +
                ", user_pwd='" + user_pwd + '\'' +
                ", last_login_time='" + last_login_time + '\'' +
                ", book_id='" + book_id + '\'' +
                ", local_book_id='" + local_book_id + '\'' +
                '}';
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_icon() {
        return Contact.FILRURL + this.user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_phone() {
        return this.user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getCreate_time() {
        return this.create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUser_pwd() {
        return this.user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getLast_login_time() {
        return this.last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getBook_id() {
        return this.book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public static User queryById(long id) {
        return DBManager.getInstance().getDaoSession().getUserDao().load(id);
    }
    public static List<User> query(WhereCondition cond, WhereCondition... condMore){
        return DBManager.getInstance().getDaoSession().getUserDao().queryBuilder().where(cond,condMore).list();
    }
    public static void updateLastTime() {
        User user = CustomSharedPreferencesManager.getInstance().getUser();
        user.setLast_login_time(DateUtil.getNowDateTimeWithSecond());
        updateUser(user);
    }

    public static void updateUser(User user) {
        CustomSharedPreferencesManager.getInstance().saveUser(user);
        DBManager.getInstance().getDaoSession().getUserDao().update(user);
    }

    public static void updateUserBook(List<Book> books) {
        updateBookLocalIdToUser(books);
        updateCurrentBookId(books);
    }

    public static void updateBookLocalIdToUser(List<Book> bookList) {
        CustomSharedPreferencesManager preferencesManager = CustomSharedPreferencesManager.getInstance();
        //在用户表数据中添加bookId
        User user = preferencesManager.getUser();
        String bookId = "";
        for (Book book : bookList) {
            bookId = Util.addToData(book.getLocal_id(), bookId);
        }
        user.setLocal_book_id(bookId);
        preferencesManager.saveUser(user);
        DBManager.getInstance().getDaoSession().getUserDao().update(user);
    }

    public static void updateCurrentBookId(List<Book> bookList) {
        CustomSharedPreferencesManager preferencesManager = CustomSharedPreferencesManager.getInstance();
        long currBookId = -1;
        if (bookList.size() != 0)
            currBookId = bookList.get(0).getLocal_id();
        preferencesManager.saveCurrBookId(currBookId);
    }

    public Setting getSetting() {
        Setting set = DBManager.getInstance().getDaoSession().getSettingDao().load(user_id);
        if (set == null) {
            set = Setting.newSetting(user_id);
            DBManager.getInstance().getDaoSession().getSettingDao().insert(set);
        }
        return set;
    }

    public String getLocal_book_id() {
        return this.local_book_id;
    }

    public void setLocal_book_id(String local_book_id) {
        this.local_book_id = local_book_id;
    }
}
