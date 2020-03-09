package com.lyh.guanbei.bean;

import com.bumptech.glide.Glide;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.query.WhereCondition;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

@Entity
public class Notification {
    @Id
    private long id;
    private long userId;
    private String title;
    private String content;
    private String data;
    private String date;
    private int type;
    private int status;
    //请求好友状态下：0代表未点击    1代表已同意  -1代表已拒绝  -2代表过期  3天过期
    @Transient
    public static final int BOOK_INVITE=1; //账本邀请请求
    @Transient
    public static final int BUDGET_WARNING=2;      //超出预算警告


    @Generated(hash = 267753965)
    public Notification(long id, long userId, String title, String content, String data, String date, int type,
            int status) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.data = data;
        this.date = date;
        this.type = type;
        this.status = status;
    }
    @Generated(hash = 1855225820)
    public Notification() {
    }


    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getData() {
        return this.data;
    }
    public void setData(String data) {
        this.data = data;
    }
    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public int getStatus() {
        return this.status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public static void save(String title, String content, String data,
                            String date){
        try {
            JSONObject jsonObject=new JSONObject(data);
            DBManager.getInstance().getDaoSession().getNotificationDao().insertOrReplaceInTx(
                    new Notification(jsonObject.getLong("timeId"), CustomSharedPreferencesManager.getInstance().getUser().getUser_id(),title,content,data,date,jsonObject.getInt("type"),0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static List<Notification> query(WhereCondition cond, WhereCondition... condMore) {
        return DBManager.getInstance().getDaoSession().getNotificationDao().queryBuilder().where(cond, condMore).list();
    }
    public void save(){
        DBManager.getInstance().getDaoSession().getNotificationDao().update(this);
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", data='" + data + '\'' +
                ", date='" + date + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
