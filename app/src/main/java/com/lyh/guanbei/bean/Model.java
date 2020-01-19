package com.lyh.guanbei.bean;

import android.content.Context;

import com.lyh.guanbei.manager.CustomSharedPreferencesManager;
import com.lyh.guanbei.manager.DBManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

@Entity
public class Model implements Serializable {
    @Transient
    public static final long serialVersionUID=33333L;
    @Id(autoincrement = true)
    private Long id;
    @Transient
    private static final String MODEL_KEY="model_init";
    private String name;
    private String date;
    private String toWho;
    private String amount;
    private String amount_Type;
    private String remark;
    @Generated(hash = 1595696348)
    public Model(Long id, String name, String date, String toWho, String amount, String amount_Type, String remark) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.toWho = toWho;
        this.amount = amount;
        this.amount_Type = amount_Type;
        this.remark = remark;
    }

    public Model(String name, String date, String toWho, String amount, String amount_Type, String remark) {
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
    public static void init(){
        CustomSharedPreferencesManager customSharedPreferencesManager=CustomSharedPreferencesManager.getInstance();
        if(!(boolean)customSharedPreferencesManager.getParam(MODEL_KEY,false)){
            //微信账单
            Model model=new Model("微信模板","交易时间","交易对方","金额(元)","收/支","备注");
            save(model);
            customSharedPreferencesManager.saveParam(MODEL_KEY,true);
        }
        //微信
    }
    public static void save(Model model){
        DBManager.getInstance().getDaoSession().getModelDao().insertOrReplace(model);
    }
    public static Model queryById(long id){
        return DBManager.getInstance().getDaoSession().getModelDao().load(id);
    }
    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", toWho='" + toWho + '\'' +
                ", amount='" + amount + '\'' +
                ", amount_Type='" + amount_Type + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    public void setId(Long id) {
        this.id = id;
    }
}
