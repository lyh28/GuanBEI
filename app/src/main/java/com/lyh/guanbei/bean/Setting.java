package com.lyh.guanbei.bean;

import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Setting {
    @Id
    private long user_id;
    private String pattern_pwd;
    private boolean notify_input;   //通知栏
    private boolean nodisturb;      //免扰模式
    private String nodisturb_start_date;    //15:00格式
    private String nodisturb_end_date;

    @Generated(hash = 1215827106)
    public Setting(long user_id, String pattern_pwd, boolean notify_input, boolean nodisturb, String nodisturb_start_date,
                   String nodisturb_end_date) {
        this.user_id = user_id;
        this.pattern_pwd = pattern_pwd;
        this.notify_input = notify_input;
        this.nodisturb = nodisturb;
        this.nodisturb_start_date = nodisturb_start_date;
        this.nodisturb_end_date = nodisturb_end_date;
    }

    @Generated(hash = 909716735)
    public Setting() {
    }

    public static Setting newSetting(long userId) {
        return new Setting(userId, "", true
                , false, "23:00", "07:00");
    }

    public void save() {
        DBManager.getInstance().getDaoSession().getSettingDao().update(this);
    }

    public long getUser_id() {
        return this.user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public String getPattern_pwd() {
        return this.pattern_pwd;
    }

    public Setting setPattern_pwd(String pattern_pwd) {
        this.pattern_pwd = pattern_pwd;
        return this;
    }

    public boolean isLocked() {
        return !getPattern_pwd().equals("");
    }

    public boolean getNotify_input() {
        return this.notify_input;
    }

    public Setting setNotify_input(boolean notify_input) {
        this.notify_input = notify_input;
        return this;
    }

    public boolean getNodisturb() {
        return this.nodisturb;
    }

    public Setting setNodisturb(boolean nodisturb) {
        this.nodisturb = nodisturb;
        return this;
    }

    public String getNodisturb_start_date() {
        return this.nodisturb_start_date;
    }

    public Setting setNodisturb_start_date(String nodisturb_start_date) {
        this.nodisturb_start_date = nodisturb_start_date;
        return this;
    }

    public String getNodisturb_end_date() {
        return this.nodisturb_end_date;
    }

    public Setting setNodisturb_end_date(String nodisturb_end_date) {
        this.nodisturb_end_date = nodisturb_end_date;
        return this;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "user_id=" + user_id +
                ", pattern_pwd='" + pattern_pwd + '\'' +
                ", notify_input=" + notify_input +
                ", nodisturb=" + nodisturb +
                ", nodisturb_start_date='" + nodisturb_start_date + '\'' +
                ", nodisturb_end_date='" + nodisturb_end_date + '\'' +
                '}';
    }
}
