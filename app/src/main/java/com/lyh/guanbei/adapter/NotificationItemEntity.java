package com.lyh.guanbei.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.lyh.guanbei.bean.Notification;
import com.lyh.guanbei.util.DateUtil;

public class NotificationItemEntity implements MultiItemEntity {
    private Notification notification;
    private int ItemType;
    private String detail;
    public static final String REJECT_DETAIL="已拒绝";
    public static final String AGREE_DETAIL="已同意";
    public static final String PAST_DETAIL="已过期";
    public static final int NORMAL=1;      //仅包含日期
    public static final int DETAIL=2;      //右边包含信息的       已拒绝  已同意
    public static final int ACTION=3;      //右边包含动作的的      忽略 同意

    public NotificationItemEntity(Notification notification) {
        this.notification = notification;
        int days= DateUtil.differentDaysAndNowWithSecond(notification.getDate());
        if(days>3){
            notification.setStatus(-2);
            notification.save();
        }
        if(notification.getType()==Notification.BUDGET_WARNING)
            this.ItemType=NORMAL;
        else{
            if(notification.getStatus()==0)
                this.ItemType=ACTION;
            else{
                this.ItemType=DETAIL;
                if(notification.getStatus()==-1)
                    detail=REJECT_DETAIL;
                else if(notification.getStatus()==1)
                    detail=AGREE_DETAIL;
                else if(notification.getStatus()==-2)
                    detail=PAST_DETAIL;
            }
        }
    }

    public void setItemType(int itemType) {
        ItemType = itemType;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public int getItemType() {
        return ItemType;
    }

    public Notification getNotification() {
        return notification;
    }

    public String getDetail() {
        return detail;
    }
}
