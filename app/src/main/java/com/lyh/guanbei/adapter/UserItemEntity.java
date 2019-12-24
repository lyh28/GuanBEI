package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lyh.guanbei.bean.UserBean;

public class UserItemEntity implements MultiItemEntity {
    private UserBean user;
    private int ItemType;
    public static final int NORMAL=1;
    public static final int ADD=2;
    public static final int DELETE=3;
    public UserItemEntity(UserBean user, int itemType) {
        this.user = user;
        ItemType = itemType;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public UserBean getUser() {
        return user;
    }
}
