package com.lyh.guanbei.bean;

import com.lyh.guanbei.common.GuanBeiApplication;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;
import java.util.List;

@Entity
public class UserBean implements Serializable {
    public static final long serialVersionUID=11111L;
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
    @Generated(hash = 1522956954)
    public UserBean(long user_id, String user_name, String user_icon,
            @NotNull String user_phone, String create_time, String user_pwd,
            String last_login_time, String book_id) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_icon = user_icon;
        this.user_phone = user_phone;
        this.create_time = create_time;
        this.user_pwd = user_pwd;
        this.last_login_time = last_login_time;
        this.book_id = book_id;
    }
    @Generated(hash = 1203313951)
    public UserBean() {
    }
    @Override
    public String toString() {
        return "UserBean{" +
                "user_id=" + user_id +
                ", user_name='" + user_name + '\'' +
                ", user_icon='" + user_icon + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", create_time='" + create_time + '\'' +
                ", user_pwd='" + user_pwd + '\'' +
                ", last_login_time='" + last_login_time + '\'' +
                ", book_id='" + book_id + '\'' +
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
        return this.user_icon;
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
    public static UserBean queryById(long id){
        return GuanBeiApplication.getDaoSession().getUserBeanDao().load(id);
    }
}
