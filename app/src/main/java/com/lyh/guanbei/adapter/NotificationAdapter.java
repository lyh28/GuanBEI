package com.lyh.guanbei.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lyh.guanbei.R;
import com.lyh.guanbei.bean.Notification;
import com.lyh.guanbei.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationAdapter extends BaseMultiItemQuickAdapter<NotificationItemEntity, BaseViewHolder> {
    private Context context;

    public NotificationAdapter(Context context) {
        super(null);
        this.context = context;
        addItemType(NotificationItemEntity.NORMAL, R.layout.listitem_notification_normal);
        addItemType(NotificationItemEntity.DETAIL, R.layout.listitem_notification_detail);
        addItemType(NotificationItemEntity.ACTION, R.layout.listitem_notification_action);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, NotificationItemEntity item) {
        Notification notification = item.getNotification();
        helper.setText(R.id.listitem_notification_title, notification.getTitle());
        helper.setText(R.id.listitem_notification_content, notification.getContent());
        helper.setText(R.id.listitem_notification_date, wrapDate(notification.getDate()));
        switch (item.getItemType()) {
            case NotificationItemEntity.ACTION:
                helper.addOnClickListener(R.id.listitem_notification_check, R.id.listitem_notification_reject);
                break;
            case NotificationItemEntity.DETAIL:
                helper.setText(R.id.listitem_notification_detail, item.getDetail());
                break;
        }
    }
    private String wrapDate(String date){
        int days=DateUtil.differentDaysAndNowWithSecond(date);
        if(days==0){
            return date.split(" ")[1];
        }else
            return days+"天前";
    }
    public void setNewDatas(@Nullable List<Notification> data) {
        List<NotificationItemEntity> list=new ArrayList<>();
        for(Notification notification:data)
            list.add(new NotificationItemEntity(notification));
        super.setNewData(list);
    }
}
