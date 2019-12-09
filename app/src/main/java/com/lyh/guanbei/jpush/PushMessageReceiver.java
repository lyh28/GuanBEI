package com.lyh.guanbei.jpush;

import android.content.Context;

import cn.jpush.android.api.CustomMessage;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.api.NotificationMessage;
import cn.jpush.android.service.JPushMessageReceiver;

public class PushMessageReceiver extends JPushMessageReceiver {
    @Override
    public void onMessage(Context context, CustomMessage customMessage) {
        super.onMessage(context, customMessage);
    }

    @Override
    public void onNotifyMessageArrived(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageArrived(context, notificationMessage);
        //通知回调

    }

    @Override
    public void onNotifyMessageOpened(Context context, NotificationMessage notificationMessage) {
        super.onNotifyMessageOpened(context, notificationMessage);
        //点击通知的回调
    }

    @Override
    public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
//        if(jPushMessage.getErrorCode()==6002)
//            JPushInterface.setAlias(context,2,"testdemo");

        super.onAliasOperatorResult(context, jPushMessage);
    }
}
