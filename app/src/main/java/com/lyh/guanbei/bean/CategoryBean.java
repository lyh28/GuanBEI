package com.lyh.guanbei.bean;

import com.lyh.guanbei.common.GuanBeiApplication;
import com.lyh.guanbei.db.CategoryBeanDao;
import com.lyh.guanbei.manager.RecordCategoryManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;

@Entity
public class CategoryBean {
    @Id(autoincrement = true)
    private Long id;
    @Index
    private String name;
    private int iconId;
    @Index
    private int type;   //分辨是收入还是支出
    @Transient
    public static final int IN=1;
    @Transient
    public static final int OUT=2;
    @Generated(hash = 322139429)
    public CategoryBean(Long id, String name, int iconId, int type) {
        this.id = id;
        this.name = name;
        this.iconId = iconId;
        this.type = type;
    }

    public CategoryBean(String name, int iconId, int type) {
        this.name = name;
        this.iconId = iconId;
        this.type = type;
    }

    @Override
    public String toString() {
        return "CategoryBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconId=" + iconId +
                ", type=" + type +
                '}';
    }

    @Generated(hash = 1870435730)
    public CategoryBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
    public static void InsertPresetInList(){
        if(CategoryBean.getCategoryByType(CategoryBean.IN).size()==0){
            GuanBeiApplication.getDaoSession().getCategoryBeanDao().insertInTx(RecordCategoryManager.getPresetInList());
        }
    }
    public static void InsertPresetOutList(){
        if(CategoryBean.getCategoryByType(CategoryBean.OUT).size()==0){
            GuanBeiApplication.getDaoSession().getCategoryBeanDao().insertInTx(RecordCategoryManager.getPresetOutList());
        }
    }
    public static List<CategoryBean> getCategoryByType(int type){
        return GuanBeiApplication.getDaoSession().getCategoryBeanDao().queryBuilder().where(
                CategoryBeanDao.Properties.Type.eq(type)).list();
    }
    public static CategoryBean getCategoryByName(String name,int type){
        List<CategoryBean> list=GuanBeiApplication.getDaoSession().getCategoryBeanDao().queryBuilder().where(CategoryBeanDao.Properties.Name.eq(name),
                CategoryBeanDao.Properties.Type.eq(type)).list();
        if(list==null||list.size()==0)
            return null;
        return list.get(0);
    }
    public Long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
