package com.lyh.guanbei.bean;

import com.lyh.guanbei.db.DBManager;
import com.lyh.guanbei.db.TagDao;
import com.lyh.guanbei.manager.TagManager;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {
    @Id(autoincrement = true)
    private Long local_id;
    private long tag_id;
    @Index
    private String name;
    private int iconId;
    @Index
    private int type;   //分辨是收入还是支出
    @Transient
    public static final int IN = 1;
    @Transient
    public static final int OUT = 2;

    public Tag(String name, int iconId, int type) {
        this.name = name;
        this.iconId = iconId;
        this.type = type;
    }

    @Generated(hash = 1808180524)
    public Tag(Long local_id, long tag_id, String name, int iconId, int type) {
        this.local_id = local_id;
        this.tag_id = tag_id;
        this.name = name;
        this.iconId = iconId;
        this.type = type;
    }

    @Generated(hash = 1605720318)
    public Tag() {
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

    public static void InsertPresetInList() {
//        if(Tag.getTagByType(Tag.IN).size()==0){
        DBManager.getInstance().getDaoSession().getTagDao().insertOrReplaceInTx(TagManager.getPresetInList());
//        }
    }

    public static void InsertPresetOutList() {
//        if(Tag.getTagByType(Tag.OUT).size()==0){
        DBManager.getInstance().getDaoSession().getTagDao().insertOrReplaceInTx(TagManager.getPresetOutList());
//        }
    }

    public static List<Tag> getTagByType(int type) {
        List<Tag> list = DBManager.getInstance().getDaoSession().getTagDao().queryBuilder().where(
                TagDao.Properties.Type.eq(type)).list();
        if (list == null) return new ArrayList<>();
        return list;
    }

    public static Tag getTagByName(String name, int type) {
        List<Tag> list = DBManager.getInstance().getDaoSession().getTagDao().queryBuilder().where(TagDao.Properties.Name.eq(name),
                TagDao.Properties.Type.eq(type)).list();
        if (list == null || list.size() == 0)
            return null;
        return list.get(0);
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getLocal_id() {
        return this.local_id;
    }

    public void setLocal_id(Long local_id) {
        this.local_id = local_id;
    }

    public long getTag_id() {
        return this.tag_id;
    }

    public void setTag_id(long tag_id) {
        this.tag_id = tag_id;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "local_id=" + local_id +
                ", tag_id=" + tag_id +
                ", name='" + name + '\'' +
                ", iconId=" + iconId +
                ", type=" + type +
                '}';
    }
}
