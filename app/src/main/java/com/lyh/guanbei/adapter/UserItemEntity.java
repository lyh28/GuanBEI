package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lyh.guanbei.bean.User;

public class UserItemEntity implements MultiItemEntity {
    private User user;
    private int ItemType;
    public static final int NORMAL=1;
    public static final int ADD=2;
    public static final int DELETE=3;
    public UserItemEntity(User user, int itemType) {
        this.user = user;
        ItemType = itemType;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public User getUser() {
        return user;
    }
}
