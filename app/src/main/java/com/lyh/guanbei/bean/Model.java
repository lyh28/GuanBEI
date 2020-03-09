package com.lyh.guanbei.bean;

import android.content.Context;

import com.lyh.guanbei.db.ModelDao;
import com.lyh.guanbei.db.RecordDao;
import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.query.WhereCondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Model implements Serializable {
    @Transient
    public static final long serialVersionUID = 33333L;
    @Id(autoincrement = true)
    private Long id;
    private long userId;
    private String name;
    private String date;
    private String toWho;
    private String amount;
    private String amount_Type;
    private String remark;
    private static String[] DEFAULT_MODEL={
            "微信模板"
    };
    public Model(String name, String date, String toWho, String amount, String amount_Type, String remark) {
        this.name = name;
        this.date = date;
        this.toWho = toWho;
        this.amount = amount;
        this.amount_Type = amount_Type;
        this.remark = remark;
    }

    @Generated(hash = 583472559)
    public Model(Long id, long userId, String name, String date, String toWho, String amount, String amount_Type,
                 String remark) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.date = date;
        this.toWho = toWho;
        this.amount = amount;
        this.amount_Type = amount_Type;
        this.remark = remark;
    }

    @Generated(hash = 2118404446)
    public Model() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToWho() {
        return this.toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public String getAmount() {
        return this.amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount_Type() {
        return this.amount_Type;
    }

    public void setAmount_Type(String amount_Type) {
        this.amount_Type = amount_Type;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public static Model getDefaultModel() {
        Model model = new Model("微信模板", "交易时间", "交易对方", "金额(元)", "收/支", "备注");
        return model;
        //微信
    }
    public static boolean isDefaultModel(Model model){
        int index=Arrays.binarySearch(DEFAULT_MODEL,model.getName());
        return index<0?false:true;
    }
    public static void save(Model model) {
        DBManager.getInstance().getDaoSession().getModelDao().insertOrReplace(model);
    }

    public static Model queryById(long id) {
        return DBManager.getInstance().getDaoSession().getModelDao().load(id);
    }
    public static List<Model> query(WhereCondition cond, WhereCondition... condMore) {
        return DBManager.getInstance().getDaoSession().getModelDao().queryBuilder().where(cond, condMore).list();
    }
    public static List<Model> queryByUserId(long userId) {
        List<Model> res=new ArrayList<>();
        //添加默认模板
        res.add(getDefaultModel());
        res.addAll(query(ModelDao.Properties.UserId.eq(CustomSharedPreferencesManager.getInstance().getUser().getUser_id())));
        return res;
    }

    public void setId(Long id) {
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
        return "Model{" +
                "id=" + id +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", toWho='" + toWho + '\'' +
                ", amount='" + amount + '\'' +
                ", amount_Type='" + amount_Type + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
